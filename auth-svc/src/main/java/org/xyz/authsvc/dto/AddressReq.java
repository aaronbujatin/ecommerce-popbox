package org.xyz.authsvc.dto;

public record AddressReq(
        String street,
        String city,
        String municipal,
        String postalCode,
        String country
) {
}
