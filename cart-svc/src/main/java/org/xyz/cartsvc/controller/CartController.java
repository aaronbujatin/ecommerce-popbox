package org.xyz.cartsvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xyz.cartsvc.dto.CartItemRequest;
import org.xyz.cartsvc.dto.CartResponse;
import org.xyz.cartsvc.repository.CartRepository;
import org.xyz.cartsvc.service.CartService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    private final CartRepository cartRepository;

    @PostMapping("/items/increment")
    public ResponseEntity<CartResponse> addCartItem(@RequestBody CartItemRequest cartItemRequest) {
        log.info("Receiving request {}", cartItemRequest);
        return ResponseEntity.ok(cartService.addCartItem(cartItemRequest));
    }

    @PostMapping("/items/decrement")
    public ResponseEntity<CartResponse> removeCartItem(@RequestBody CartItemRequest cartItemRequest) {
        log.info("Receiving request {}", cartItemRequest);
        return ResponseEntity.ok(cartService.removeCartItem(cartItemRequest));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> getAllCarts(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(cartService.getCartByUserId(id));
    }


}
