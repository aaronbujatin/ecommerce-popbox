package org.xyz.usersvc.dto;

public record RegisterCustomerRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        AddressRequest addressRequest
) {
}
