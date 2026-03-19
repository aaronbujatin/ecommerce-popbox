package org.xyz.usersvc.dto;

public record CustomerSignupReq(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        String stringifyReq
) {
}
