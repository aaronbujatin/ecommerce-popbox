package org.xyz.usersvc.service.customer;

import org.xyz.usersvc.dto.*;

import java.util.List;

public interface CustomerService {

    void createCustomer(CustomerSignupReq customerSignupReq);
    CustomerResponse getCustomerById(Long id);
    CustomerResponse getAuthCustomerByEmail(String email);
    List<CustomerResponse> getAllUsers();
    CustomerResponse updateUser(RegisterCustomerRequest registerCustomerRequest);
    String deleteUserById(Long id);
    CustomerResponse getAuthLoginInfo(LoginCustomerRequest customerRequest);
}

