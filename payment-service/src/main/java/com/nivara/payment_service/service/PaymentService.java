package com.nivara.payment_service.service;

import com.nivara.payment_service.model.dto.ConfirmPaymentRequestDTO;
import com.nivara.payment_service.model.dto.ConfirmPaymentResponseDTO;
import com.nivara.payment_service.model.dto.CreatePaymentRequestDTO;
import com.nivara.payment_service.model.dto.CreatePaymentResponseDTO;

public interface PaymentService {

    public CreatePaymentResponseDTO createPayment(CreatePaymentRequestDTO requestBody);   
    public ConfirmPaymentResponseDTO confirmPayment(ConfirmPaymentRequestDTO requestBody);

}
