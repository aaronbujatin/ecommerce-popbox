package org.xyz.cartsvc.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.xyz.cartsvc.enums.CartErrorInfo;

@Getter
public class ProductOutOfStockException extends CartException{

    public ProductOutOfStockException(CartErrorInfo productErrorInfo) {
        super(productErrorInfo);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

}
