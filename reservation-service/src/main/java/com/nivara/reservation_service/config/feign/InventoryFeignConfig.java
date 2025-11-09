package com.nivara.reservation_service.config.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.codec.ErrorDecoder;

@Configuration
public class InventoryFeignConfig {
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new InventoryErrorDecoder();
    }

}
