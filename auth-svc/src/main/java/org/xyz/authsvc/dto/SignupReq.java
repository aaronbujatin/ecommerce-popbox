package org.xyz.authsvc.dto;

public record SignupReq(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone
) {
}
