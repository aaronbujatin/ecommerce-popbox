package org.xyz.usersvc.dto;

import org.xyz.usersvc.entity.Customer;

public record AddressResponse(
        Long id,
        Customer customerId,
        String street,
        String city,
        String municipal,
        String postalCode,
        String country
) {
}
