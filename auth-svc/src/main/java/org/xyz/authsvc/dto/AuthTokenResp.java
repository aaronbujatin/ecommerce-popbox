package org.xyz.authsvc.dto;

public record AuthTokenResp(
        String accessToken,
        long expiryTimeInSeconds,
        String username,
        String imageUrl
) {
}
