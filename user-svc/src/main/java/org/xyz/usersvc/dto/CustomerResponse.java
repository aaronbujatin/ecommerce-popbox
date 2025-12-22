package org.xyz.usersvc.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isActive
) {
}
