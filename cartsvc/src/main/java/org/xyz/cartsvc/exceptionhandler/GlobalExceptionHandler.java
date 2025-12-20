package org.xyz.cartsvc.exceptionhandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xyz.cartsvc.dto.ApiErrorResponse;
import org.xyz.cartsvc.enums.CartErrorInfo;
import org.xyz.cartsvc.exception.CartException;
import org.xyz.cartsvc.exception.ProductOutOfStockException;
import org.xyz.cartsvc.exception.ResourceNotFoundException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
                {
                    ResourceNotFoundException.class,
                    ProductOutOfStockException.class
                }
            )
    public ResponseEntity<ApiErrorResponse> handleCartExceptions(
            CartException ex,
            HttpServletRequest request
    ) {
        CartErrorInfo productErrorInfo = ex.getCartErrorInfo();
        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(
                    productErrorInfo.getCode(),
                    productErrorInfo.getMessage(),
                    Instant.now(),
                    request.getRequestURI()
                );

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(apiErrorResponse);
    }



}