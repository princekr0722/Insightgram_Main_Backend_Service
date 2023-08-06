package com.Insightgram.config;

import java.util.Base64;
import java.util.Base64.Encoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilConfigurations {

	@Bean
    Encoder base64Encoder() {
        return Base64.getEncoder();
    }

//	@Bean
//    ErrorDecoder errorDecoder() {
//        return new CustomErrorDecoder();
//    }
	
}
