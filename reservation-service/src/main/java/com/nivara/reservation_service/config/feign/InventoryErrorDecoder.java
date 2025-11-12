package com.nivara.reservation_service.config.feign;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.nivara.reservation_service.exception.HoldDuplicateException;
import com.nivara.reservation_service.exception.HoldReleasedException;
import com.nivara.reservation_service.exception.InventoryUnavailableException;
import com.nivara.reservation_service.exception.RemoteServerException;

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
            case 409 -> new HoldDuplicateException("Hold already exits: " + body);
            case 410 -> new HoldReleasedException("Hold Expired & Released: " + body);  
            case 422 -> new InventoryUnavailableException("Inventory Unavailable: " + body);
            case 500, 502, 503, 504 -> new RemoteServerException("Inventory Service Failure: " + body);  
            default  -> defaultDecoder.decode(methodKey, response); 
        };
    }



}
