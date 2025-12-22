package org.xyz.usersvc.dto;

public record CustomerInfoResp(
        Long id,
        String name,
        String email,
        String phone,
        boolean isActive
) {
}
