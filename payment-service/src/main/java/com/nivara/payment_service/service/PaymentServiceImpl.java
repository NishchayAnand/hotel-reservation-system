package com.nivara.payment_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.nivara.payment_service.client.InventoryServiceClient;
import com.nivara.payment_service.exception.HoldConfirmedException;
import com.nivara.payment_service.exception.HoldExpiredException;
import com.nivara.payment_service.exception.HoldExpiringSoonException;
import com.nivara.payment_service.exception.HoldReleaseException;
import com.nivara.payment_service.exception.InventoryServiceException;
import com.nivara.payment_service.exception.InventoryUnavailableException;
import com.nivara.payment_service.exception.PaymentConfirmedException;
import com.nivara.payment_service.exception.PaymentFailedException;
import com.nivara.payment_service.model.dto.HoldDTO;
import com.nivara.payment_service.model.dto.CreatePaymentRequestDTO;
import com.nivara.payment_service.model.dto.CreatePaymentResponseDTO;
import com.nivara.payment_service.model.dto.CustomerDTO;
import com.nivara.payment_service.model.entity.Payment;
import com.nivara.payment_service.model.enums.HoldStatus;
import com.nivara.payment_service.model.enums.PaymentStatus;
import com.nivara.payment_service.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private final PaymentRepository paymentRepository;
    private final InventoryServiceClient inventoryServiceClient;
    private final RazorpayClient razorpayClient;

    /*
     * Creates a Razorpay Order for the given reservation.
     * 
     * @param reservationId reservation identifier
     * @param holdId hold id from inventory service
     * @param amount amount
     * @param currency ISO currency code ("INR")
     * @param customer DTO with name / email / phone
     * @return CreatePaymentResponse containing providerOrderId
    */
    @Override
    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO requestBody) throws Exception {

        // ---------- Step 1: Inventory Hold Validation ----------
        // this remote call will be retried for transient exceptions
        HoldDTO hold = inventoryServiceClient.getHold(requestBody.holdId()); // throws InventoryUnavailableException, InventoryServiceException (if all retries have been exhausted)
        HoldStatus holdStatus = hold.getStatus();
        
        switch (holdStatus) {

            case CONFIRMED: {
                log.info("Confirmed hold means payment was processed successfully. Continue since the idempotency check will return the existing payment info");
                throw new HoldConfirmedException("Hold was already confirmed. Please check your booking before retrying");
            }

            case RELEASED: {
                log.info("released hold means the hold was expired and the selected inventory was released for re-booking");
                throw new HoldReleaseException("Hold has been released. Cannot proceed with payment.");
            }

            case HELD: {
                Instant expiresAt = hold.getExpiresAt();
                if(Instant.now().isAfter(expiresAt)) {
                    throw new HoldExpiredException("Hold has already expired.");

                } else if (Instant.now().isAfter(expiresAt.minus(2, ChronoUnit.MINUTES))) {
                    throw new HoldExpiringSoonException("Hold is expiring soon. Refresh the hold before requesting payment again.");
                }
                log.info("Hold is valid atleast for the next 2 minutes. Continue to create payment"); 
            }

        }
        
        // ---------- Step 2: Idempotency Check ----------
        Optional<Payment> existingOpt = paymentRepository.findByReservationId(requestBody.reservationId());
        Payment paymentRecord = null;

        if(existingOpt.isPresent()) {
            Payment existing = existingOpt.get();
            PaymentStatus paymentStatus = existing.getStatus();

            switch (paymentStatus) {

                case COMPLETED:
                    throw new PaymentConfirmedException("Previous payment attempt was already confirmed. Please check your booking before retrying");

                case FAILED:
                    throw new PaymentFailedException("Previous payment attempt failed.");

                case PENDING:
                    return CreatePaymentResponseDTO.builder()
                        .providerOrderId(existing.getProviderOrderId())
                        .status(PaymentStatus.PENDING)
                        .message("Payment order has already created.")
                        .build();
           
                case CREATED:
                    log.info("Payment record already exist but the payment order hasn't been created yet. Continue but reuse existing record.");
                    paymentRecord = existing;

            }

        }

        // Step 3: Create Payment record (PENDING)
        Payment payment = Payment.builder()
            .reservationId(requestBody.reservationId())
            .total(requestBody.total())
            .currency(requestBody.currency())
            .status(PaymentStatus.PENDING)
            .customer(requestBody.customer())
            .build();
        payment = paymentRepository.save(payment);

        try {
            // Build Razorpay Order payload (amount MUST be in paise/smallest unit)
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", requestBody.getAmount() * 100);
            orderRequest.put("currency", requestBody.getCurrency() != null ? requestBody.getCurrency() : "INR");
            orderRequest.put("receipt", "hold_" + requestBody.getHoldId());
            orderRequest.put("payment_capture", 0);

            Order order = razorpayClient.orders.create(orderRequest);
            payment.setProviderOrderId(order.get("id"));
            paymentRepository.save(payment);

            return CreatePaymentResponseDTO.builder()
                .providerOrderId(order.get("id"))
                .build();

        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);

            return CreatePaymentResponseDTO.builder()
                .status(PaymentStatus.FAILED)
                .message("gateway error: " + e.getMessage())
                .build();
        }
        
    }

}
