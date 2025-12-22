package org.xyz.usersvc.service.customer;

import org.xyz.usersvc.dto.RegisterCustomerRequest;
import org.xyz.usersvc.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(RegisterCustomerRequest registerCustomerRequest);
    CustomerResponse getUserById(Long id);
    List<CustomerResponse> getAllUsers();
    CustomerResponse updateUser(RegisterCustomerRequest registerCustomerRequest);
    String deleteUserById(Long id);
}
