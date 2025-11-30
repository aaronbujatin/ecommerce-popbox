package org.xyz.usersvc.dto;

public record LoginCustomerRequest(
        String email,
        String password
) {
}
