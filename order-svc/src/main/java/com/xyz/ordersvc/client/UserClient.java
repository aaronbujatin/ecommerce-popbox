package com.xyz.ordersvc.client;

import com.xyz.ordersvc.client.dto.UserResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://localhost:8083")
public interface UserClient {

    @GetMapping("/api/v1/customers/{id}")
    Optional<UserResp> getUserByById(@PathVariable("id") Long id);


}
