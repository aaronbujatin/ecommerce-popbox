package org.xyz.usersvc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.xyz.usersvc.exception.ValidationRequestException;

public record LoginCustomerRequest(
        String email,
        String password
) {

//    public LoginCustomerRequest {
//
//        if (email.isBlank() || email.isEmpty() || email == null) {
//            throw ValidationRequestException("valid email is required.")
//        }
//
//    }
}
