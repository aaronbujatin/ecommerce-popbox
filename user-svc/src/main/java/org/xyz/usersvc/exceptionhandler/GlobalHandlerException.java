package org.xyz.usersvc.exceptionhandler;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.xyz.usersvc.dto.ApiErrorResponse;

import java.time.Instant;

@RestControllerAdvice
public class GlobalHandlerException {

//    @ExceptionHandler(
//            {
//                    BadRequestException.class
//            }
//    )
//    public ResponseEntity<ApiErrorResponse> handleCartExceptions(
//            CartException ex,
//            HttpServletRequest request
//    ) {
//        CartErrorInfo productErrorInfo = ex.getCartErrorInfo();
//        ApiErrorResponse apiErrorResponse =
//                new ApiErrorResponse(
//                        productErrorInfo.getCode(),
//                        productErrorInfo.getMessage(),
//                        Instant.now(),
//                        request.getRequestURI()
//                );
//
//        return ResponseEntity
//                .status(ex.getHttpStatus())
//                .body(apiErrorResponse);
//    }



}