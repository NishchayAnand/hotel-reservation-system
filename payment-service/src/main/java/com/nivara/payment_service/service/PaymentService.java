package com.nivara.payment_service.service;

import com.nivara.payment_service.model.dto.PaymentRequestDTO;
import com.nivara.payment_service.model.dto.PaymentResponseDTO;

public interface PaymentService {

    public PaymentResponseDTO createPayment(String requestId, PaymentRequestDTO requestBody);    

}
