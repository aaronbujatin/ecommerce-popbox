package org.xyz.cartsvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xyz.cartsvc.dto.CartItemRequest;
import org.xyz.cartsvc.dto.CartResponse;
import org.xyz.cartsvc.service.CartServiceImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

//    private final CartService cartService;
    private final CartServiceImpl cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addCartItem(@RequestBody CartItemRequest cartItemRequest) {
        return ResponseEntity.ok(cartService.addCartItem(cartItemRequest));
    }



}
