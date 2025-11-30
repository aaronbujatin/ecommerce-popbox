package org.xyz.cartsvc.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.xyz.cartsvc.dto.ProductClientResponse;

import java.util.Optional;

@FeignClient(name = "product-service", url = "http://localhost:8081")
public interface ProductClient {

    @GetMapping("/api/v1/products/{id}")
    Optional<ProductClientResponse> getProductById(@PathVariable("id") Long id);

}
