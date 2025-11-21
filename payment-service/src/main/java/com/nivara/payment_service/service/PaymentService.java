package com.nivara.payment_service.service;

import com.nivara.payment_service.model.dto.ConfirmPaymentRequest;
import com.nivara.payment_service.model.dto.CreatePaymentRequestDTO;
import com.nivara.payment_service.model.dto.CreatePaymentResponseDTO;

public interface PaymentService {

    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO requestBody);   
    public void handlePaymentCallback(ConfirmPaymentRequest requestBody);

}
