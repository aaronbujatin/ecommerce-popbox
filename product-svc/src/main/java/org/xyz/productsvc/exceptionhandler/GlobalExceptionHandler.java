package org.xyz.productsvc.exceptionhandler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xyz.productsvc.dto.ApiErrorResponse;
import org.xyz.productsvc.enums.ProductErrorInfo;
import org.xyz.productsvc.exception.ResourceNotFoundException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleProductNotFound(
                ResourceNotFoundException ex,
                HttpServletRequest request
            ) {

        ProductErrorInfo productErrorInfo = ex.getProductErrorInfo();

        ApiErrorResponse apiErrorResponse =
                new ApiErrorResponse(
                        productErrorInfo.getCode(),
                        productErrorInfo.getMessage(),
                        Instant.now(),request.getRequestURI()
                );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(apiErrorResponse);
    }

}
