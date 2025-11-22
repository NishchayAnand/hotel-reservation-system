package com.nivara.payment_service.util;

import org.json.JSONObject;

import com.razorpay.Utils;

public class RazorpaySignatureVerifier {

    private final String secret;

    public RazorpaySignatureVerifier(String secret) {
        this.secret = secret;
    }

    public boolean isValidSignature(String orderId, String paymentId, String signature) {
        try {
            // prepare the options JSONObject with order_id, payment_id and signature
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", orderId);
            options.put("razorpay_payment_id", paymentId);
            options.put("razorpay_signature", signature);

            // Verify the payment signature
            return Utils.verifyPaymentSignature(options, secret);

        } catch (Exception ex) {
            return false;
        }
    }

}
