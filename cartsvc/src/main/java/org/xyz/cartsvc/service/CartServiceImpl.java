package org.xyz.cartsvc.service;

import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartMapper cartMapper;
    private final UserClient userClient;
    private final ProductClient productClient;

    public CartResponse addCartItem(CartItemRequest cartItemRequest) {
        Long cartId;

        //If cart is active and existing
        if (cartItemRequest.cartId() != null) {

            Cart cart = cartRepository.findById(cartItemRequest.cartId())
                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND));

            //TODO: Asses if user is till need
//            UserClientResponse user = userClient.getUserById(cartItemRequest.userId())
//                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

            ProductClientResponse product = new ProductClientResponse(1L, 10, BigDecimal.valueOf(100.00));
//            ProductClientResponse product = productClient.getProductById(cartItemRequest.userId())
//                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

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
            //cart id is null, create new instance of product
            UserClientResponse user = new UserClientResponse(1L);
//            UserClientResponse user = userClient.getUserById(cartItemRequest.userId())
//                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

            ProductClientResponse product = new ProductClientResponse(1L, 10, BigDecimal.valueOf(100.00));
//            ProductClientResponse product = productClient.getProductById(cartItemRequest.userId())
//                    .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

            if (cartItemRequest.quantity() >= product.stock()) {
                throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
            }

            Cart cart = new Cart();
            cart.setUserId(user.id());
            cart.setStatus(CartStatus.ACTIVE);
            cart.setCreatedAt(LocalDateTime.now());
            Cart savedCart = cartRepository.save(cart);

            CartItem cartItem = new CartItem();
            cartItem.setCart(savedCart);
            cartItem.setProductId(cartItemRequest.productId());
            cartItem.setQuantity(cartItemRequest.quantity());
            cartItem.setTotalPrice(calculateTotalPrice(cartItemRequest.quantity() , product.price()));
            cartItem.setAddedAt(LocalDateTime.now());
            cartItemRepository.save(cartItem);

            cartId = savedCart.getId();
        }

        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        return new CartResponse(
                    cartId,
                    calculateCartItemTotalPrice(cartItems),
                    cartMapper.mapListToCartItemResponse(cartItems)
            );

    }

    @Override
    public CartResponse removeCartItem(CartItemRequest cartItemRequest) {
        return null;
    }


}
