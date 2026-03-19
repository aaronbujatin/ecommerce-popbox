package com.xyz.ordersvc.exception;

import com.xyz.ordersvc.enums.OrderErrorInfo;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    private final OrderErrorInfo orderErrorInfo;

    public ResourceNotFoundException(OrderErrorInfo orderErrorInfo) {
        super(orderErrorInfo.getMessage());
        this.orderErrorInfo = orderErrorInfo;
    }

}

