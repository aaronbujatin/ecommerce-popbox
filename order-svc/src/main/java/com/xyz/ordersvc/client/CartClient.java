package com.xyz.ordersvc.client;

import com.xyz.ordersvc.dto.ExtCartResponse;
import com.xyz.ordersvc.dto.ExtCartConvertRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@FeignClient(name = "cart-service", url = "http://localhost:8082")
public interface CartClient {

    @PostMapping("/api/v1/carts/items/user/convert")
    Optional<ExtCartResponse> convertCart(@RequestBody ExtCartConvertRequest extCartConvertRequest);

}
