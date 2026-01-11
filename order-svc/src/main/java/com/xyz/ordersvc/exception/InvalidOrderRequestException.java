package com.xyz.ordersvc.exception;

import com.xyz.ordersvc.enums.OrderErrorInfo;
import lombok.Getter;

@Getter
public class InvalidOrderRequestException extends RuntimeException{

    private final OrderErrorInfo orderErrorInfo;

    public InvalidOrderRequestException(OrderErrorInfo orderErrorInfo) {
        super(orderErrorInfo.getMessage());
        this.orderErrorInfo = orderErrorInfo;
    }



}
