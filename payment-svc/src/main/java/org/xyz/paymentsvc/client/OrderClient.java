package org.xyz.paymentsvc.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment-service", url = "http://localhost:8086")
public interface OrderClient {




}
