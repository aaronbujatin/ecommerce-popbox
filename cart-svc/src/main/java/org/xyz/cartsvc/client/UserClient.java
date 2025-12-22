package org.xyz.cartsvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xyz.cartsvc.dto.UserClientResponse;

import java.util.Optional;

@FeignClient(name = "user-service", url = "http://localhost:8083")
public interface UserClient {

    @GetMapping("/api/v1/customers/{id}")
    Optional<UserClientResponse> getUserById(@PathVariable("id") Long id);

}
