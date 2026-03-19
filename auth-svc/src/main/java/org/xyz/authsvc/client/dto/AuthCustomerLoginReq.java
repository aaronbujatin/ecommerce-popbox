package org.xyz.authsvc.client.dto;

public record AuthCustomerLoginReq(
        String email,
        String password
) {
}
