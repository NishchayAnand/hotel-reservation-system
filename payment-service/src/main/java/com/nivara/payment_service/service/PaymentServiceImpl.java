package com.nivara.payment_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.nivara.payment_service.client.InventoryServiceClient;
import com.nivara.payment_service.exception.InventoryServiceException;
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
    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO requestBody) {
        
        // Step 1: Idempotency Check: If payment already exists, return it.
        Optional<Payment> existing = paymentRepository.findByReservationId(requestBody.reservationId());
        if(existing.isPresent()) {
            Payment payment = existing.get();
            PaymentStatus paymentStatus = payment.getStatus();
            if(paymentStatus != PaymentStatus.CREATED) {
                CreatePaymentResponseDTO response = CreatePaymentResponseDTO.from(payment);
                switch (paymentStatus) {
                    case PENDING    -> response.setMessage("Payment order already created");
                    case COMPLETED  -> response.setMessage("Payment already completed");
                    case FAILED     -> response.setMessage("Previous payment attempt failed");
                    default         -> response.setMessage("Existing payment with status: " + paymentStatus);
                }
                return response;
            }
        }

        // Step 2: Inventory Check: If inventory hold has already expired, throw InventoryUnavailableException
        HoldDTO hold;
        try {
            // this remote call will be retried for transient exceptions
            hold = inventoryServiceClient.getHold(requestBody.holdId());
            HoldStatus holdStatus = hold.getStatus();
        } catch (InventoryServiceException ex) {
            
        }
        
        if(!"ACTIVE".equalsIgnoreCase(String.valueOf(hold.getStatus()))) {
            return CreatePaymentResponseDTO.builder()
                .status(PaymentStatus.FAILED)
                .message("hold is not active")
                .build();
        }

        // safety margin: ensure hold has at least 2 minutes remaining (avoid redirect and immediate expiry)
        Instant expiresAt = hold.getExpiresAt();
        if(Instant.now().isAfter(expiresAt.minus(2, ChronoUnit.MINUTES))) {
            return CreatePaymentResponseDTO.builder()
                .status(PaymentStatus.FAILED)
                .message("hold is expiring soon or already expired; refresh hold before paying again")
                .build();
        }

        // Step 3: Create Payment record (PENDING)
        Payment payment = Payment.builder()
            .requestId(requestId)
            .customer(requestBody.getCustomer())
            .amount(requestBody.getAmount())
            .currency(requestBody.getCurrency())
            .status(PaymentStatus.PENDING)
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
