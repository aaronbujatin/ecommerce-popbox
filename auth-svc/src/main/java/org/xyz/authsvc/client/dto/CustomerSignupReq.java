package org.xyz.authsvc.client.dto;

import java.util.Set;

public record CustomerSignupReq(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone,
        String stringifyReq
) {
}
