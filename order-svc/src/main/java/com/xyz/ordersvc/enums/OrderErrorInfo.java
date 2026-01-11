package com.xyz.ordersvc.enums;

import lombok.Getter;

@Getter
public enum OrderErrorInfo {

   ORDER_NOT_FOUND("ORD0404", "Order not found"),
   USER_NOT_FOUND("PR0405", "User id not found"),
   PRODUCT_NOT_FOUND("PR0406", "Product id not found"),
   PRODUCT_UNIT_NOT_FOUND("PR0408", "Product unit id not found"),
   PRODUCT_OUT_OF_STOCK("PR0407", "Product out of stock"),
   PRODUCT_UNIT_OUT_OF_STOCK("PR0409", "Product unit out of stock"),
   ORDER_NOT_ACTIVE("PR0408", "Order is not active"),
   ORDER_ITEM_NOT_FOUND("ORD0411", "Order item not found"),
   ORDER_ITEM_NOT_ACTIVE("ORD0412", "Order item was not active"),
   USER_ORDER_DOES_NOT_EXIST("CR0410", "User cart does not exist");

    private final String code;
    private final String message;

    OrderErrorInfo(String code, String message) {
        this.code = code;
        this.message = message;
    }


}