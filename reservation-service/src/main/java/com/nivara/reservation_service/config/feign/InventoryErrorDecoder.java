package com.nivara.reservation_service.config.feign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.nivara.reservation_service.exception.InventoryUnavailableException;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;

public class InventoryErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = "";
        try {
            if (response.body() != null) {
                body = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            }
        } catch (IOException ex) {}
        
        int status = response.status();
        return switch(status) {
            case 409 -> new InventoryUnavailableException("Inventory Unavailable: " + body);
            default  -> defaultDecoder.decode(methodKey, response); 
        };
    }



}
