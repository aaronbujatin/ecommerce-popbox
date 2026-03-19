package org.xyz.usersvc.controller;

import feign.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xyz.usersvc.dto.*;
import org.xyz.usersvc.exception.ResourceNotFoundException;
import org.xyz.usersvc.repository.CustomerRepository;
import org.xyz.usersvc.service.customer.CustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

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

    @PostMapping("/create")
    public ResponseEntity<String> createCustomer(@RequestBody CustomerSignupReq customerSignupReq) {
            customerService.createCustomer(customerSignupReq);

        return ResponseEntity.ok("Successfully saved customer");
    }

    @PostMapping("/login")
    public ResponseEntity<CustomerResponse> loginAuthenticate(@Valid @RequestBody LoginCustomerRequest loginCustomerRequest) {
        System.out.println("testhere1234");
        return ResponseEntity.ok(customerService.getAuthLoginInfo(loginCustomerRequest));
    }

    @PostMapping("/login2")
    public ResponseEntity<String> loginAuthenticate2(@Valid @RequestBody LoginCustomerRequest loginCustomerRequest) {
        System.out.println("testhere1234");
        return ResponseEntity.ok("test");
    }

}
