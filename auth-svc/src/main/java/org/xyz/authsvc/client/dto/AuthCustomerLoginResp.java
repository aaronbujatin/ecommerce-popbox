package org.xyz.authsvc.client.dto;

import java.util.Set;

public record AuthCustomerLoginResp(
        Long id,
        String email,
        Set<String> roles
) {

}
