package org.xyz.cartsvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.xyz.cartsvc.repository.CartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.xyz.cartsvc.util.CartUtil.calculateCartItemTotalPrice;
import static org.xyz.cartsvc.util.CartUtil.calculateTotalPrice;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final UserClient userClient;
    private final ProductClient productClient;

    public CartResponse addCartItem(CartItemRequest cartItemRequest) {

        UserClientResponse userExtResp = this.userClient.getUserById(cartItemRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        ProductClientResponse productExtResp = this.productClient.getProductById(cartItemRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

        if (cartItemRequest.quantity() >= productExtResp.stock() ) {
            throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
        }

        Cart cart = cartRepository.findByUserId(userExtResp.id())
                .orElseGet(() -> {
                    log.info("🛒No cart for user id [{}]. ✅Creating new cart", userExtResp.id());
                    var newCart = Cart
                            .builder()
                            .userId(userExtResp.id())
                            .status(CartStatus.ACTIVE)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return cartRepository.save(newCart);
                });

        var cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProductId().equals(productExtResp.id()))
                .findFirst()
                .map(item -> {
                    log.info("🔁Updating cart item with product id [{}].", productExtResp.id());
                    var quantity = item.getQuantity() + cartItemRequest.quantity();
                    var cartItemTotalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

                    if (quantity >= productExtResp.stock()) {
                        throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
                    }

                    item.setQuantity(quantity);
                    item.setTotalPrice(cartItemTotalPrice);
                    item.setUpdatedAt(LocalDateTime.now());
                    return item;
                })
                .orElseGet(() -> {
                    log.info("🙅No existing cart item for product id {}. ✅Creating new cart item", productExtResp.id());
                    return CartItem
                            .builder()
                            .cart(cart)
                            .productId(cartItemRequest.productId())
                            .unitPrice(productExtResp.price())
                            .quantity(cartItemRequest.quantity())
                            .totalPrice(calculateTotalPrice(cartItemRequest.quantity(), productExtResp.price()))
                            .addedAt(LocalDateTime.now())
                            .build();
                });

        cart.addToCart(cartItem);
        cartRepository.save(cart);

        return new CartResponse(
                cart.getId(),
                calculateCartItemTotalPrice(cart.getCartItems()),
                null
        );
    }

    @Override
    public CartResponse removeCartItem(CartItemRequest cartItemRequest) {
        UserClientResponse userExtResp = this.userClient.getUserById(cartItemRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        ProductClientResponse productExtResp = this.productClient.getProductById(cartItemRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

        if (cartItemRequest.quantity() >= productExtResp.stock() ) {
            throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
        }

        Cart cart = cartRepository.findByUserId(userExtResp.id())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND));

       cart.getCartItems()
                .stream()
                .filter(item -> item.getProductId().equals(productExtResp.id()))
                .findFirst()
                .map(item -> {
                    if (item.getQuantity() != 1) {
                        log.info("🔁Updating cart item quantity with product id [{}].", productExtResp.id());
                        var quantity = item.getQuantity() - cartItemRequest.quantity();
                        var cartItemTotalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
                        item.setQuantity(quantity);
                        item.setTotalPrice(cartItemTotalPrice);
                        item.setUpdatedAt(LocalDateTime.now());
                    } else {
                        log.info("✖️Removing to cart item with product id [{}].", productExtResp.id());
                        cart.removeToCart(item);
                    }

                    return cartRepository.save(cart);
                })
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));



        var cartItems = cart.getCartItems();
        var productIds = cartItems
                .stream()
                .map(CartItem::getProductId)
                .toList();

        var productExtRespList = productClient.getAllProductById(productIds)
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));
        return new CartResponse(
                cart.getId(),
                calculateCartItemTotalPrice(cartItems),
                cartItemResponseMapper(cartItems, productExtRespList)
        );
    }

    @Override
    public CartResponse getCartByUserId(Long id) {
        log.info("👤Getting the cart by user id {}", id);

        UserClientResponse userExtResp = userClient.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUserId(userExtResp.id())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND));

        List<CartItem> cartItems = cart.getCartItems();

        var productIds = cartItems
                .stream()
                .map(CartItem::getProductId)
                .toList();

        List<ProductClientResponse> productExtRespList = productClient.getAllProductById(productIds)
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

        return new CartResponse(
                cart.getId(),
                calculateCartItemTotalPrice(cartItems),
                cartItemResponseMapper(cartItems, productExtRespList)
        );
    }


    private List<CartItemResponse> cartItemResponseMapper(List<CartItem> cartItems, List<ProductClientResponse> productClientResponses) {
        return cartItems
                .stream()
                .map(cartItem -> {

                    var productExtResp = productClientResponses
                            .stream()
                            .filter(product -> product.id().equals(cartItem.getProductId()))
                            .findFirst()
                            .orElseThrow();

                    return new CartItemResponse(
                            cartItem.getProductId(),
                            productExtResp.name(),
                            cartItem.getUnitPrice(),
                            productExtResp.images().get(0),
                            cartItem.getQuantity(),
                            cartItem.getTotalPrice()
                    );
                })
                .collect(Collectors.toList());
    }

    private List<CartItemResponse> cartItemResponseMapper(List<CartItem> cartItems, ProductClientResponse productExtResp) {
        return cartItems
                .stream()
                .map(cartItem ->
                     new CartItemResponse(
                            cartItem.getProductId(),
                            productExtResp.name(),
                            cartItem.getUnitPrice(),
                            productExtResp.images().get(0),
                            cartItem.getQuantity(),
                            cartItem.getTotalPrice()

                ))
                .collect(Collectors.toList());
    }

}
