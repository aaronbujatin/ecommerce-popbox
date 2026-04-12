package org.xyz.authsvc.dto;

public record RefreshTokenReq(
        Long userId,
        String tokenId
) {
}
