package org.xyz.authsvc.client.dto;

public record UserResp(
        String name,
        String username,
        String imageUrl
) {
}
