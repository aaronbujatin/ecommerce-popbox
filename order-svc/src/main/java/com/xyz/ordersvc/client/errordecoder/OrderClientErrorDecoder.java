package com.xyz.ordersvc.client.errordecoder;

import com.xyz.ordersvc.enums.OrderErrorInfo;
import com.xyz.ordersvc.exception.InvalidOrderRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class OrderClientErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()) {
            case 400:
                return new InvalidOrderRequestException(OrderErrorInfo.ORDER_ITEM_NOT_ACTIVE);
            default:
                return defaultDecoder.decode(methodKey, response);
        }
    }
}
