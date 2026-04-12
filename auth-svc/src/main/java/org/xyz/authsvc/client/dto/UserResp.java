package org.xyz.authsvc.client.dto;

import java.util.Set;

public record UserResp(
        Long id,
        String email,
        String password,
        Set<String> roles
) {
}
