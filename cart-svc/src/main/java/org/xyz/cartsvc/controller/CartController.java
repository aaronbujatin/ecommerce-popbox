package org.xyz.cartsvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xyz.cartsvc.dto.CartConvertRequest;
import org.xyz.cartsvc.dto.CartItemRequest;
import org.xyz.cartsvc.dto.CartItemResponse;
import org.xyz.cartsvc.dto.CartResponse;
import org.xyz.cartsvc.service.CartService;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin("http://localhost:4200")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

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

    @GetMapping("/items/user/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable("userId") Long id) {
        return ResponseEntity.ok(cartService.getCartByUserId(id));
    }

    @PostMapping("/items/user/convert")
    public ResponseEntity<CartResponse> convertCart(@RequestBody CartConvertRequest cartConvertRequest) {
        return ResponseEntity.ok(cartService.convertCart(cartConvertRequest));
    }



//    @GetMapping("/items/user/test")
//    public ResponseEntity<CartResponse> test() {
//
//        var cart = new CartResponse(1L, 1L, BigDecimal.valueOf(10.00),
//                List.of(
//                        new CartItemResponse(1L, 1L, "test name", BigDecimal.valueOf(1000.00), "test image", 10, BigDecimal.valueOf(1000.00)),
//                        new CartItemResponse(2L, 2L, "test name", BigDecimal.valueOf(1000.00), "test image", 10, BigDecimal.valueOf(1000.00))
//                    )
//                );
//
//        return ResponseEntity.ok(cartService.convertCart(cartConvertRequest));
//    }

}
