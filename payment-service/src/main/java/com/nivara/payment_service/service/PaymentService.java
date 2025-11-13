package com.nivara.payment_service.service;

import com.nivara.payment_service.model.dto.CreatePaymentOrderResponseDTO;

public interface PaymentService {

    public CreatePaymentOrderResponseDTO createPayment(Long reservationId, long amount, String currency);    

}
