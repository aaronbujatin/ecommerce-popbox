package org.xyz.cartsvc.enums;

import lombok.Getter;

@Getter
public enum CartErrorInfo {

   CART_NOT_FOUND("PR0404", "Cart not found"),
   USER_NOT_FOUND("PR0405", "User id not found"),
   PRODUCT_NOT_FOUND("PR0406", "Product id not found"),
    PRODUCT_OUT_OF_STOCK("PR0407", "Product out of stock");

    private final String code;
    private final String message;

    CartErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }


}