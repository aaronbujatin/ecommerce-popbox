package org.xyz.usersvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xyz.usersvc.dto.CustomerInfoResp;
import org.xyz.usersvc.exception.ResourceNotFoundException;
import org.xyz.usersvc.repository.CustomerRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping("/{id}")
    public ResponseEntity<CustomerInfoResp> getCustomerInfo(@PathVariable("id") Long id) {

        var customer = customerRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        return ResponseEntity.ok(
                new CustomerInfoResp(
                        customer.getId(),
                        customer.getFirstName(),
                        customer.getEmail(),
                        customer.getPhone(),
                        customer.isActive()
                )
        );
    }

}
