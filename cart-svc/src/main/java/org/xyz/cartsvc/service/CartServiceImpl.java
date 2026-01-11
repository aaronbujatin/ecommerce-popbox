package org.xyz.cartsvc.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.xyz.cartsvc.client.ProductClient;
import org.xyz.cartsvc.client.UserClient;
import org.xyz.cartsvc.dto.*;
import org.xyz.cartsvc.dto.external.*;
import org.xyz.cartsvc.entity.Cart;
import org.xyz.cartsvc.entity.CartItem;
import org.xyz.cartsvc.enums.CartErrorInfo;
import org.xyz.cartsvc.enums.CartItemStatus;
import org.xyz.cartsvc.enums.CartReqStatus;
import org.xyz.cartsvc.exception.InvalidCartRequestException;
import org.xyz.cartsvc.exception.ResourceNotFoundException;
import org.xyz.cartsvc.repository.CartItemRepository;
import org.xyz.cartsvc.repository.CartRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.xyz.cartsvc.util.CartUtil.calculateCartItemTotalPrice;
import static org.xyz.cartsvc.util.CartUtil.calculateTotalPrice;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserClient userClient;
    private final ProductClient productClient;
    private final CartItemRepository cartItemRepository;

    public CartResponse addCartItem(CartItemRequest cartItemRequest) {

        UserResponse extUserResp = this.userClient.getUserById(cartItemRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        ProductResponse extProductResp = this.productClient.getProductById(cartItemRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

        var extProductUnitResp = extProductResp.productUnits()
                .stream()
                .filter(unit -> unit.id().equals(cartItemRequest.productUnitId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_UNIT_NOT_FOUND));

        if (cartItemRequest.quantity() >= extProductUnitResp.stock()) {
            throw new InvalidCartRequestException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
        }

        Cart cart = cartRepository.findByUserId(extUserResp.id())
                .orElseGet(() -> {
                    log.info("🛒No cart for user id [{}]. ✅Creating new cart", extUserResp.id());
                    var newCart = Cart
                            .builder()
                            .userId(extUserResp.id())
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    return cartRepository.save(newCart);
                });


        var cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProductId().equals(extProductResp.id())
                        && item.getProductUnitId().equals(extProductUnitResp.id())
                        && item.getStatus().equals(CartItemStatus.ACTIVE)
                )
                .findFirst()
                .map(item -> {
                    log.info("🔁Updating item with product id [{}].", extProductResp.id());
                    var quantity = item.getQuantity() + cartItemRequest.quantity();
                    var cartItemTotalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(quantity));

                    if (quantity >= extProductUnitResp.stock()) {
                        throw new InvalidCartRequestException(CartErrorInfo.PRODUCT_OUT_OF_STOCK);
                    }

                    item.setQuantity(quantity);
                    item.setTotalPrice(cartItemTotalPrice);
                    item.setUpdatedAt(LocalDateTime.now());
                    return item;
                })
                .orElseGet(() -> {
                    log.info("🙅No existing cart item for product id {}. ✅Creating new cart item", extProductResp.id());
                    return CartItem
                            .builder()
                            .cart(cart)
                            .status(CartItemStatus.ACTIVE)
                            .productId(extProductResp.id())
                            .unitPrice(extProductUnitResp.price())
                            .productUnitId(extProductUnitResp.id())
                            .quantity(cartItemRequest.quantity())
                            .totalPrice(calculateTotalPrice(cartItemRequest.quantity(), extProductUnitResp.price()))
                            .build();
                    }
                );

        cart.addToCart(cartItem);
        cartRepository.save(cart);

        return new CartResponse(
                cart.getId(),
                extUserResp.id(),
                cart.getCartItems().stream().map(CartItem::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add),
                cart.getCartItems()
                        .stream()
                        .map(item -> new CartItemResponse(
                                    item.getId(),
                                    item.getProductId(),
                                    item.getProductUnitId(),
                                    extProductResp.name(),
                                    item.getUnitPrice(),
                                    extProductResp.images().get(0),
                                    extProductUnitResp.productUnitType(),
                                    item.getQuantity(),
                                    item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())),null
                                )
                        )
                        .toList(),
                null
        );
    }

    @Override
    public CartResponse removeCartItem(CartItemRequest cartItemRequest) {
        UserResponse extUserResp = this.userClient.getUserById(cartItemRequest.userId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        ProductResponse extProductResp = this.productClient.getProductById(cartItemRequest.productId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

        var extProductUnitResp = extProductResp.productUnits()
                .stream()
                .filter(unit -> unit.id().equals(cartItemRequest.productUnitId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_UNIT_NOT_FOUND));

        if (cartItemRequest.quantity() >= extProductUnitResp.stock()) {
            throw new InvalidCartRequestException(CartErrorInfo.PRODUCT_UNIT_OUT_OF_STOCK);
        }

        Cart cart = cartRepository.findByUserId(extUserResp.id())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND));

        cart.getCartItems()
                .stream()
                .filter(item -> item.getProductId().equals(extProductResp.id())
                        && item.getProductUnitId().equals(extProductUnitResp.id())
                        && item.getStatus().equals(CartItemStatus.ACTIVE)
                )
                .findFirst()
                .map(item -> {
                    if (item.getQuantity() != 1) {
                        log.info("🔁Updating cart item quantity with product id [{}].", extProductUnitResp.id());
                        var quantity = item.getQuantity() - cartItemRequest.quantity();
                        var cartItemTotalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(quantity));
                        item.setQuantity(quantity);
                        item.setTotalPrice(cartItemTotalPrice);
                        item.setUpdatedAt(LocalDateTime.now());
                    } else {
                        log.info("✖️Removing to cart item with product id [{}].", extProductUnitResp.id());
                        cart.removeToCart(item);
                    }

                    return cartRepository.save(cart);
                })
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_ITEM_NOT_FOUND));

        return new CartResponse(
                cart.getId(),
                extUserResp.id(),
                calculateCartItemTotalPrice(cart.getCartItems()),
                cart.getCartItems()
                        .stream()
                        .map(item -> new CartItemResponse(
                                        item.getId(),
                                        item.getProductId(),
                                        item.getProductUnitId(),
                                        extProductResp.name(),
                                        item.getUnitPrice(),
                                        null,
                                        extProductUnitResp.productUnitType(),
                                        item.getQuantity(),
                                        item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())),null
                                )
                        )
                        .toList(),
                null
        );
    }

    @Override
    public CartResponse getCartByUserId(Long id) {
        log.info("👤Getting the cart by user id {}", id);

        UserResponse userExtResp = userClient.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUserId(userExtResp.id())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.CART_NOT_FOUND));

        var cartItems = cart.getCartItems()
                .stream()
                .filter(ci -> ci.getStatus() == CartItemStatus.ACTIVE)
                .sorted(Comparator.comparing(CartItem::getId))
                .toList();

        var productBatchReqs = cartItems.stream()
                .collect(Collectors.groupingBy(
                        CartItem::getProductId,
                        Collectors.mapping(CartItem::getProductUnitId, Collectors.toList())
                    )
                )
                .entrySet()
                .stream()
                .map(entry -> new ProductBatchReq(
                            entry.getKey(),
                            entry.getValue()
                        )
                )
                .toList();

        var productExtRespList = productClient.getAllProductByBatchIds(productBatchReqs)
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));

        return new CartResponse(
                cart.getId(),
                userExtResp.id(),
                calculateCartItemTotalPrice(cartItems),
                cartItemResponseMapper(cartItems, productExtRespList),
                null
        );
    }

    @Override
    public CartResponse convertCart(CartConvertRequest request) {

        Cart cart = cartRepository.findByUserId(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.USER_NOT_FOUND));

        var cartItems = cartItemRepository.findByIdInAndStatus(request.cartItemIds(), CartItemStatus.ACTIVE);

        if (cartItems.isEmpty()) {
            throw new InvalidCartRequestException(CartErrorInfo.CART_ITEM_NOT_ACTIVE);
        }

        cartItems
                .forEach(cartItem -> {
                        cartItem.setStatus(CartItemStatus.BEGIN_CHECKOUT);
                        cartItem.setConvertedAt(LocalDateTime.now());
                    }
                );

        var productBatchReqs = cartItems.stream()
                .collect(Collectors.groupingBy(
                                CartItem::getProductId,
                                Collectors.mapping(CartItem::getProductUnitId, Collectors.toList())
                        )
                )
                .entrySet()
                .stream()
                .map(entry -> new ProductBatchReq(
                                entry.getKey(),
                                entry.getValue()
                        )
                )
                .toList();

        var productExtRespList = productClient.getAllProductByBatchIds(productBatchReqs)
                .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_NOT_FOUND));
        var cartResponse = new CartResponse(
                cart.getId(),
                cart.getUserId(),
                calculateCartItemTotalPrice(cart.getCartItems()),
                cartItemResponseMapper(cartItems, productExtRespList),
                CartReqStatus.CONVERSION_SUCCESS
        );

        cartRepository.save(cart);
        log.info("🔁Cart item with product id of [{}] successfully converted. Message: ",
                CartItemStatus.BEGIN_CHECKOUT.getDescription());
        return cartResponse;
    }

    private boolean isCartItemsAvailable(CartItem cartItem, Map<Long, List<Long>> productReqs) {
        return productReqs.containsKey(cartItem.getProductId()) &&
                productReqs.get(cartItem.getProductId()).contains(cartItem.getProductUnitId());
    }

    private boolean isCartItemsNotCheckout(CartItem cartItem) {
        if (cartItem.getStatus().equals(CartItemStatus.BEGIN_CHECKOUT)) {
            throw new IllegalStateException("Cart items already checkout");
        }

        return true;
    }



    private List<CartItemResponse> cartItemResponseMapper(List<CartItem> cartItems, List<ProductBatchResp> productBatchResponses) {
        System.out.println(cartItems);
        System.out.println(productBatchResponses);

        return cartItems
                .stream()
                .map(cartItem -> {
                    var productBatchResp = productBatchResponses
                            .stream()
                            .filter(product -> product.id().equals(cartItem.getProductId()))
                            .findFirst()
                            .orElseThrow();

                    var productUnit = productBatchResp.productUnits()
                            .stream()
                            .filter(productUnitBatchResp -> productUnitBatchResp.id().equals(cartItem.getProductUnitId()))
                            .findFirst()
                            .orElseThrow(() -> new ResourceNotFoundException(CartErrorInfo.PRODUCT_UNIT_NOT_FOUND));

                    return new CartItemResponse(
                            cartItem.getId(),
                            cartItem.getProductId(),
                            cartItem.getProductUnitId(),
                            productBatchResp.name(),
                            cartItem.getUnitPrice(),
                            productUnit.imageUrl(),
                            productUnit.unitType(),
                            cartItem.getQuantity(),
                            cartItem.getTotalPrice(),
                            cartItem.getStatus()
                    );
                })
                .collect(Collectors.toList());
    }


}
