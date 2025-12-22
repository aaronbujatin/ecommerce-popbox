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
import org.xyz.cartsvc.repository.CartItemRepository;
import org.xyz.cartsvc.repository.CartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        List<CartItem> cartItems;

        Optional<CartItem> cartItemOpt = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productExtResp.id());

        if (cartItemOpt.isPresent()) {
            log.info("🔁Updating cart item with product id [{}].", cartItemRequest.productId());

            var cartItem = cartItemOpt.get();
            var quantity = cartItem.getQuantity() + cartItemRequest.quantity();
            var cartItemTotalPrice = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

            if (quantity >= productExtResp.stock()) {
                throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
            }

            cartItem.setQuantity(quantity);
            cartItem.setTotalPrice(cartItemTotalPrice);
            cartItem.setUpdatedAt(LocalDateTime.now());

            cartItemRepository.save(cartItem);
        } else {
            log.info("🙅No existing cart item for product id {}. ✅Creating new cart item", productExtResp.id());
            var newCartItem = CartItem
                    .builder()
                    .cart(cart)
                    .productId(cartItemRequest.productId())
                    .unitPrice(productExtResp.price())
                    .quantity(cartItemRequest.quantity())
                    .totalPrice(calculateTotalPrice(cartItemRequest.quantity(), productExtResp.price()))
                    .addedAt(LocalDateTime.now())
                    .build();

            cartItemRepository.save(newCartItem);
        }

        cartItems = cartItemRepository.findByCartId(cart.getId());

        return new CartResponse(
                cart.getId(),
                calculateCartItemTotalPrice(cartItems),
                null
        );
    }

    @Override
    public CartResponse removeCartItem(CartItemRequest cartItemRequest) {
        log.info("🔁removeCartItem cart item with product id [{}].", cartItemRequest.productId());
        UserClientResponse userExtResp = this.userClient.getUserById(cartItemRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        ProductClientResponse productExtResp = this.productClient.getProductById(cartItemRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

        if (cartItemRequest.quantity() >= productExtResp.stock() ) {
            throw new ProductOutOfStockException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
        }

        Cart cart = cartRepository.findByUserId(userExtResp.id())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND));


        Optional<CartItem> cartItemOpt = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productExtResp.id());

        if (cartItemOpt.isPresent()) {

            var cartItem = cartItemOpt.get();

            if (cartItem.getQuantity() == 1) {
                log.info("🔁Deleting cart item with product id [{}].", productExtResp.id());
                cartItemRepository.deleteById(cartItem.getId());
            } else {
                log.info("🔁Removing cart item with product id [{}].", productExtResp.id());
                var quantity = cartItem.getQuantity() - cartItemRequest.quantity();
                var cartItemTotalPrice = cartItem.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

                cartItem.setQuantity(quantity);
                cartItem.setTotalPrice(cartItemTotalPrice);
                cartItem.setUpdatedAt(LocalDateTime.now());
                cartItemRepository.save(cartItem);
            }
        } else {
            throw new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND);
        }

        var cartItems = cartItemRepository.findByCartId(cart.getId());
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

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

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
