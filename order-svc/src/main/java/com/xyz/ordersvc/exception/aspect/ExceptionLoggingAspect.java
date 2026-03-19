package com.xyz.ordersvc.exception.aspect;

import com.xyz.ordersvc.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class ExceptionLoggingAspect {

    @AfterReturning(
            pointcut = "execution(* com.xyz.ordersvc.exception.handler.GlobalExceptionHandler.*(..))",
            returning = "response"
    )
    public void logErrorResponse(Object response) {
        if (response instanceof ResponseEntity<?> entity
                && entity.getBody() instanceof ApiErrorResponse body) {
            log.warn("Exception occurred, returning error response: {}", body);
        }
    }


}
