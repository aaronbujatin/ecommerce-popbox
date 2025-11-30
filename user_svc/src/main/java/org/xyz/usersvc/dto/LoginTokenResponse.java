package org.xyz.usersvc.dto;

public record LoginTokenResponse(
        String token,
        long expiresIn
) {
}
