package org.xyz.cartsvc.client.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xyz.cartsvc.client.logger.FeignClientLogger;

@Configuration
public class FeignClientConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    Logger feignClientLogger() {
        return new FeignClientLogger();
    }

}