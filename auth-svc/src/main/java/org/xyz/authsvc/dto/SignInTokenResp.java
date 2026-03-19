package org.xyz.authsvc.dto;

public record SignInTokenResp(
        String token,
        Long expireAt
) {
}
