package org.xyz.usersvc.dto;

import java.time.LocalDateTime;
import java.util.List;

public record CustomerResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        String phone,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean isActive,
        List<String> roles
) {
}
