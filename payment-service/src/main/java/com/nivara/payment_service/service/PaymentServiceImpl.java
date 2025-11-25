package com.nivara.payment_service.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nivara.payment_service.client.InventoryServiceClient;
import com.nivara.payment_service.client.ReservationServiceClient;
import com.nivara.payment_service.exception.HoldExpiredException;
import com.nivara.payment_service.exception.HoldExpiringSoonException;
import com.nivara.payment_service.exception.HoldReleaseException;
import com.nivara.payment_service.exception.PaymentFailedException;
import com.nivara.payment_service.exception.PaymentInvalidException;
import com.nivara.payment_service.exception.PaymentNotFoundException;
import com.nivara.payment_service.model.dto.HoldDTO;
import com.nivara.payment_service.model.dto.ConfirmPaymentRequestDTO;
import com.nivara.payment_service.model.dto.ConfirmPaymentResponseDTO;
import com.nivara.payment_service.model.dto.ConfirmReservationResponseDTO;
import com.nivara.payment_service.model.dto.ConsumeHoldResponseDTO;
import com.nivara.payment_service.model.dto.CreatePaymentRequestDTO;
import com.nivara.payment_service.model.dto.CreatePaymentResponseDTO;
import com.nivara.payment_service.model.entity.Payment;
import com.nivara.payment_service.model.enums.HoldStatus;
import com.nivara.payment_service.model.enums.PaymentStatus;
import com.nivara.payment_service.repository.PaymentRepository;
import com.nivara.payment_service.util.RazorpaySignatureVerifier;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.transaction.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key-id:}")
    private String razorpayKeyId;

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private PaymentRepository paymentRepository;
    private InventoryServiceClient inventoryServiceClient;
    private ReservationServiceClient reservationServiceClient;
    private RazorpayClient razorpayClient;
    private RazorpaySignatureVerifier signatureVerifier;

    public PaymentServiceImpl (
        PaymentRepository paymentRepository,
        InventoryServiceClient inventoryServiceClient,
        ReservationServiceClient reservationServiceClient,
        RazorpayClient razorpayClient,
        RazorpaySignatureVerifier signatureVerifier
    ) {
        this.paymentRepository = paymentRepository;
        this.inventoryServiceClient = inventoryServiceClient;
        this.reservationServiceClient = reservationServiceClient;
        this.razorpayClient = razorpayClient;
        this.signatureVerifier = signatureVerifier;
    }

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

        // ---------- Step 1: Inventory Hold Validation ----------
        // this remote call will be retried for transient exceptions
        HoldDTO hold = inventoryServiceClient.getHold(requestBody.holdId()); // throws InventoryUnavailableException, InventoryServiceException (if all retries have been exhausted)
        HoldStatus holdStatus = hold.getStatus();
        
        switch (holdStatus) {

            case EXPIRED -> {
                log.info("Hold {} is EXPIRED.");
                throw new HoldReleaseException("Hold has expired. Cannot proceed with payment.");
            }

            case RELEASED -> {
                log.info("Hold {} is RELEASED. Inventory was released for re-booking.");
                throw new HoldReleaseException("Hold has been released. Cannot proceed with payment.");
            }

            case ACTIVE -> {
                Instant now = Instant.now();
                Instant expiresAt = hold.getExpiresAt();

                if(now.isAfter(expiresAt)) {
                    log.info("Hold {} has EXPIRED at {}.", hold.getId(), expiresAt);
                    throw new HoldExpiredException("Hold has already expired.");

                } 
                
                if (now.isAfter(expiresAt.minus(2, ChronoUnit.MINUTES))) {
                    log.info("Hold {} is expiring soon at {}.", hold.getId(), expiresAt);
                    throw new HoldExpiringSoonException("Hold is expiring soon. Refresh the hold before requesting payment again.");
                }

                log.info("Hold {} is HELD and valid at least 2 more minutes. Proceeding to payment creation.", hold.getId()); 
            }

            case CONSUMED -> {
                // Do NOT throw here. Just log
                log.info("Hold {} is already CONFIRMED. Payment was processed earlier", hold.getId());
                // We'll rely on the payment idempotency check below to return the existing payment info.
            }

        }
        
        Payment paymentRecord = null;

        // ---------- Step 2: Idempotency Check ----------
        Optional<Payment> existingOpt = paymentRepository.findByReservationId(requestBody.reservationId());

        if(existingOpt.isPresent()) {
            Payment existing = existingOpt.get();
            PaymentStatus paymentStatus = existing.getStatus();

            switch (paymentStatus) {

                case FAILED:
                    throw new PaymentFailedException("Previous payment attempt failed.");

                case REVERSED:
                    // the payment was refunded ....
                    return CreatePaymentResponseDTO.builder()
                        .providerOrderId(existing.getProviderOrderId())
                        .status(PaymentStatus.REVERSED)
                        .message("Payment for this reservation was already refunded")
                        .build();

                case AUTHORIZED:
                    // if payment is authorized, backend will confirm hold and reservation
                    return CreatePaymentResponseDTO.builder()
                        .providerOrderId(existing.getProviderOrderId())
                        .status(PaymentStatus.AUTHORIZED)
                        .message("Previous payment attempt was already authorized. Backend is confirming the reservation")
                        .build();

                case COMPLETED:
                    // if you detect that payment is already confirmed, then the desired final outcome is already achieved.
                    // Throwing an exception would incorrectly treat this as an error — but it’s not an error. It’s simply an idempotent success.
                    return CreatePaymentResponseDTO.builder()
                        .providerOrderId(existing.getProviderOrderId())
                        .status(PaymentStatus.COMPLETED)
                        .message("Previous payment attempt was already confirmed. Please check your booking before retrying")
                        .build();
                
                case PENDING:
                    return CreatePaymentResponseDTO.builder()
                        .providerOrderId(existing.getProviderOrderId())
                        .status(PaymentStatus.PENDING)
                        .message("Payment order was already created. Waiting for PSP callback.")
                        .build();
           
                case INITIATED:
                    log.info("Payment record already exist but the payment order hasn't been created yet. Continue but reuse existing record.");
                    paymentRecord = existing;

            }

        } 
        
        else {
        // ---------- Step 3: Persist payment intent (CREATED) ----------
            Payment toCreate = Payment.builder()
                .reservationId(requestBody.reservationId())
                .holdId(requestBody.holdId())
                .amount(requestBody.amount())
                .currency(requestBody.currency())
                .status(PaymentStatus.INITIATED)
                .guestName(requestBody.guestName())
                .guestEmail(requestBody.guestEmail())
                .guestPhone(requestBody.guestPhone())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

            try {
                paymentRecord = paymentRepository.save(toCreate);

            } catch (DataIntegrityViolationException dive) {
                // concurrent insert -> another thread/process created the Payment row.
                Payment existing = paymentRepository.findByReservationId(requestBody.reservationId())
                    .orElseThrow(() -> dive);

                // If the concurrent record is still in INITIATED state, wait briefly for it to progress (polling).
                if (existing.getStatus() == PaymentStatus.INITIATED) {
                    final int maxAttempts = 10;
                    final long sleepMs = 200L;
                    int attempt = 0;
                    while (attempt++ < maxAttempts) {
                        try {
                            Thread.sleep(sleepMs);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                        existing = paymentRepository.findByReservationId(requestBody.reservationId())
                            .orElse(existing);
                        if (existing.getStatus() != PaymentStatus.INITIATED) break;
                    }
                }

                // Now decide based on observed state
                switch (existing.getStatus()) {

                    case FAILED:
                        throw new PaymentFailedException("Previous payment attempt failed.");

                    case REVERSED:
                        // the payment was refunded ....
                        return CreatePaymentResponseDTO.builder()
                            .providerOrderId(existing.getProviderOrderId())
                            .status(PaymentStatus.REVERSED)
                            .message("Payment for this reservation was already refunded")
                            .build();

                    case AUTHORIZED:
                        // if payment is authorized, backend will confirm hold and reservation
                        return CreatePaymentResponseDTO.builder()
                            .providerOrderId(existing.getProviderOrderId())
                            .status(PaymentStatus.AUTHORIZED)
                            .message("Previous payment attempt was already authorized. Backend is confirming the reservation")
                            .build();

                    case COMPLETED:
                        // if you detect that payment is already confirmed, then the desired final outcome is already achieved.
                        // Throwing an exception would incorrectly treat this as an error — but it’s not an error. It’s simply an idempotent success.
                        return CreatePaymentResponseDTO.builder()
                            .providerOrderId(existing.getProviderOrderId())
                            .status(PaymentStatus.COMPLETED)
                            .message("Previous payment attempt was already confirmed. Please check your booking before retrying")
                            .build();
                    
                    case PENDING:
                        return CreatePaymentResponseDTO.builder()
                            .providerOrderId(existing.getProviderOrderId())
                            .status(PaymentStatus.PENDING)
                            .message("Payment order has already created.")
                            .build();
            
                    case INITIATED:
                        // Concurrent worker did not progress in time; conservatively treat as processing.
                        // Return a pending-ish response so caller can retry / poll.
                        return CreatePaymentResponseDTO.builder()
                            .status(PaymentStatus.PENDING)
                            .message("Payment is being processed, please poll status.")
                            .build();

                }

            }
                
        }
        
        // ---------- Step 4: Create provider order and update payment to PENDING ----------
        try {
            // build order request JSON according to Razorpay Orders API
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", requestBody.amount() * 100); // amount MUST be in paise/smallest unit
            orderRequest.put("currency", requestBody.currency() != null ? requestBody.currency() : "INR");
            orderRequest.put("receipt", "reservation_" + requestBody.reservationId());
            orderRequest.put("payment_capture", 1); // to we explored later - when we create the finalize workflow

            Order order = razorpayClient.orders.create(orderRequest);

            if (paymentRecord != null) {
                paymentRecord.setProviderOrderId(order.get("id"));
                paymentRecord.setStatus(PaymentStatus.PENDING);
                paymentRecord.setUpdatedAt(Instant.now());
                paymentRepository.save(paymentRecord);

                return CreatePaymentResponseDTO.builder()
                    .providerOrderId(order.get("id"))
                    .providerKeyId(razorpayKeyId)
                    .status(PaymentStatus.PENDING)
                    .message("Payment order created")
                    .build();

            } else {
                log.warn("paymentRecord is null; skipping update to FAILED for reservation {}", requestBody.reservationId());
                return CreatePaymentResponseDTO.builder()
                    .status(PaymentStatus.FAILED)
                    .message("Payment record does not exist.")
                    .build();
            }
            
        } catch (RazorpayException ex) {
            log.error("Failed to create payment order for reservation {}: {}", requestBody.reservationId(), ex.getMessage(), ex);
            if (paymentRecord != null) {
                paymentRecord.setStatus(PaymentStatus.FAILED);
                paymentRecord.setUpdatedAt(Instant.now());
                paymentRepository.save(paymentRecord);
            } else {
                log.warn("paymentRecord is null; skipping update to FAILED for reservation {}", requestBody.reservationId());
            }

            return CreatePaymentResponseDTO.builder()
                .status(PaymentStatus.FAILED)
                .message("Payment gateway error. Retry later")
                .build();
        }
        
    }

    /**
     * 1. Verify Razorpay signature
     * 2. Mark payment as SUCCESS_CLIENT_CALLBACK (or FAILED_SIGNATURE)
     * 3. Orchestrate downstream calls:
     *      a) consumeHold(holdId)
     *      b) confirmReservation(reservationId, paymentId)
     * 4. Mark payment as SUCCESS / FAILED_*
     */
    @Override
    @Transactional
    public ConfirmPaymentResponseDTO confirmPayment(ConfirmPaymentRequestDTO requestBody) {
        // Step 1: Find payment by provider's order id
        Payment payment = paymentRepository
            .findByProviderOrderId(requestBody.providerOrderId())
            .orElseThrow(() -> new PaymentNotFoundException("Payment not found for order id"));

        // Step 2: Verify signature
        boolean isValid = signatureVerifier.isValidSignature(
            payment.getProviderOrderId(), // do not use razorpayOrderId returned by Checkout
            requestBody.providerPaymentId(),
            requestBody.providerSignature()
        );

        if(!isValid) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            payment.setUpdatedAt(Instant.now());
            throw new PaymentInvalidException("Invalid Razorpay signature");
        }

        // Step 3: Mark payment as AUTHORIZED and persist provider's paymentId and signature  
        payment.setProviderPaymentId(requestBody.providerPaymentId());
        payment.setProviderSignature(requestBody.providerSignature());
        payment.setStatus(PaymentStatus.AUTHORIZED);
        payment.setUpdatedAt(Instant.now());
        paymentRepository.save(payment);

        // Step 4: Consume hold via inventory-service
        try {
            ConsumeHoldResponseDTO consumeHoldResponse = inventoryServiceClient.consumeHold(payment.getHoldId(), payment.getReservationId(), payment.getId());
            if(!consumeHoldResponse.isSuccess()) throw new PaymentFailedException("Failed to consume hold: " + consumeHoldResponse.holdId());
        
        } catch (Exception ex) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(Instant.now());
            paymentRepository.save(payment);
            throw ex;

        }

        // Step 5: Confirm reservation via reservation-service
        try {
            ConfirmReservationResponseDTO confirmReservationResponse = reservationServiceClient.confirmReservation(payment.getReservationId(), payment.getId());
            if(!confirmReservationResponse.isSuccess()) throw new PaymentFailedException("Failed to confirm reservation: " + confirmReservationResponse.reservationId());
        
        } catch (Exception ex) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(Instant.now());
            paymentRepository.save(payment);
            throw ex;
            
        }

        // Step 6: Mark payment as COMPLETED
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setUpdatedAt(Instant.now());
        paymentRepository.save(payment);

        return new ConfirmPaymentResponseDTO(
            payment.getId(), 
            PaymentStatus.COMPLETED, 
            "Payment confirmed"
        );

    }

}
