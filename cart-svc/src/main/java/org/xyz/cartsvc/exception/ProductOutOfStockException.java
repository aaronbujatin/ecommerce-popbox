package org.xyz.cartsvc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.xyz.cartsvc.enums.CartErrorInfo;

@Getter
public class ProductOutOfStockException extends RuntimeException{

    private final CartErrorInfo cartErrorInfo;

    public ProductOutOfStockException(CartErrorInfo cartErrorInfo) {
        super(cartErrorInfo.getMessage());
        this.cartErrorInfo = cartErrorInfo;
    }



}
