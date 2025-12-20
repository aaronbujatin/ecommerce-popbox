package org.xyz.cartsvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.xyz.cartsvc.client.ProductClient;
import org.xyz.cartsvc.client.UserClient;
import org.xyz.cartsvc.dto.*;
import org.xyz.cartsvc.entity.Cart;
import org.xyz.cartsvc.entity.CartItem;
import org.xyz.cartsvc.enums.CartErrorInfo;
import org.xyz.cartsvc.enums.CartStatus;
import org.xyz.cartsvc.exception.ProductOutOfStockException;
import org.xyz.cartsvc.exception.ResourceNotFoundException;
import org.xyz.cartsvc.mapper.CartMapper;
import org.xyz.cartsvc.repository.CartItemRepository;
import org.xyz.cartsvc.repository.CartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.xyz.cartsvc.util.CartUtil.calculateCartItemTotalPrice;
import static org.xyz.cartsvc.util.CartUtil.calculateTotalPrice;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final UserClient userClient;
    private final ProductClient productClient;

    public CartResponse addCartItem(CartItemRequest cartItemRequest) {
        Long cartId = 1L;
        //If cart is active and existing
        if (cartItemRequest.cartId() != null) {

            Cart cart = cartRepository.findById(cartItemRequest.cartId())
                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND));

            //TODO: Asses if user is till need
//            UserClientResponse user = userClient.getUserById(cartItemRequest.userId())
//                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

//            ProductClientResponse product = new ProductClientResponse(1L, 10, BigDecimal.valueOf(100.00));
            ProductClientResponse product = productClient.getProductById(cartItemRequest.userId())
                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

            if (cartItemRequest.quantity() >= product.stock()) {
                throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
            }

            CartItem cartItemEntity = cartItemRepository.findByProductId(cartItemRequest.productId());

            //if productId exist in cartItem
            //then update the quantity and price
            if (cartItemEntity != null) {
                int requestTotalQuantity = cartItemEntity.getQuantity() + cartItemRequest.quantity();
                if (requestTotalQuantity > product.stock()) {
                    throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
                }

                cartItemEntity.setQuantity(requestTotalQuantity);
                cartItemEntity.setTotalPrice(calculateTotalPrice(requestTotalQuantity, product.price()));
                cartItemEntity.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(cartItemEntity);
            } else {
                //if product don't exist, create a new instance of cartItem
                if (cartItemRequest.quantity() >= product.stock()) {
                    throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
                }

                CartItem cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setProductId(cartItemRequest.productId());
                cartItem.setQuantity(cartItemRequest.quantity());
                cartItem.setTotalPrice(calculateTotalPrice(cartItem.getQuantity(), product.price()));
                cartItem.setAddedAt(LocalDateTime.now());
                cartItemRepository.save(cartItem);
            }

            cartId = cartItemRequest.cartId();
        } else {


            UserClientResponse user = userClient.getUserById(cartItemRequest.userId())
                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

            ProductClientResponse product = productClient.getProductById(cartItemRequest.productId())
                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

            if (cartItemRequest.quantity() >= product.stock()) {
                throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
            }


            cartRepository.findByUserId(user.id())
                    .orElseGet( test -> {
return null;
                    }, () -> {

                    });

             var test = cartRepository.findByUserId(user.id())
                    .orElseGet(existingCart -> {
                        log.info("Cart exist with id {}", user.id());
                        //if cart exist, modify the cart items

                    }, () -> {
                        log.info("Creating cart transaction");
                        //else create new cart
                        var cart = Cart
                                .builder()
                                .userId(cartItemRequest.userId())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();
                        cartRepository.save(cart);

                        var cartItem = CartItem
                                .builder()
                                .cart(cart)
                                .productId(cartItemRequest.productId())
                                .unitPrice(product.price())
                                .quantity(cartItemRequest.quantity())
                                .totalPrice(calculateTotalPrice(
                                        cartItemRequest.quantity(),
                                        product.price())
                                )
                                .addedAt(LocalDateTime.now())
                                .build();

                                cartItemRepository.save(cartItem);

                                return new CartResponse(
                                        1L,
                                        calculateCartItemTotalPrice(List.of(cartItem)),
                                        List.of(new CartItemResponse(
                                                product.productId(),
                                                product.name(),
                                                product.price(),
                                                product.image(),
                                                cartItemRequest.quantity(),
                                                calculateTotalCartItemPrice(cartItemRequest.quantity(), product.price())
                                            )
                                        )
                                );
                            }
                    );
        }

//        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
//
//        return new CartResponse(
//                    cartId,
//                    calculateCartItemTotalPrice(cartItems),
//                    cartMapper.mapListToCartItemResponse(cartItems)
//            );

        return null;
    }

    @Override
    public CartResponse removeCartItem(CartItemRequest cartItemRequest) {
        return null;
    }

    private BigDecimal calculateTotalCartItemPrice(int quantity, BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }


}
