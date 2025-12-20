package org.xyz.cartsvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xyz.cartsvc.dto.CartItemRequest;
import org.xyz.cartsvc.dto.CartResponse;
import org.xyz.cartsvc.service.CartServiceImpl;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

//    private final CartService cartService;
    private final CartServiceImpl cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addCartItem(@RequestBody CartItemRequest cartItemRequest) {
        log.info("Receiving request {}", cartItemRequest);
        return ResponseEntity.ok(cartService.addCartItem(cartItemRequest));
    }



}
