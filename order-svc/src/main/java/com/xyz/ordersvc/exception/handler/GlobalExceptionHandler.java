package com.xyz.ordersvc.exception.handler;

import com.xyz.ordersvc.dto.ApiErrorResponse;
import com.xyz.ordersvc.exception.InvalidOrderRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler({
            MethodArgumentNotValidException.class,
    })
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        var apiErrorResponse = new ApiErrorResponse(
                ex.getMessage(),
                ex.getBindingResult().getFieldError().getDefaultMessage(),
                Instant.now(),
                HttpStatus.BAD_REQUEST
        );

        logApiErrorResponse(apiErrorResponse);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiErrorResponse);
    }

    @ExceptionHandler({
            InvalidOrderRequestException.class
    })
    public ResponseEntity<ApiErrorResponse> handleInvalidCartRequestException(
            InvalidOrderRequestException ex) {

        var apiErrorResponse = new ApiErrorResponse(
                ex.getOrderErrorInfo().getCode(),
                ex.getOrderErrorInfo().getMessage(),
                Instant.now(),
                HttpStatus.BAD_REQUEST
        );

        logApiErrorResponse(apiErrorResponse);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(apiErrorResponse);
    }

    private void logApiErrorResponse(ApiErrorResponse apiErrorResponse) {
        log.warn("Exception occur, returning error response : {}", apiErrorResponse);
    }



}