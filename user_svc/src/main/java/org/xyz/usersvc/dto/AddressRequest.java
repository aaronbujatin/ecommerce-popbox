package org.xyz.usersvc.dto;

public record AddressRequest(
        String street,
        String city,
        String municipal,
        String postalCode,
        String country
) {
}
