package org.xyz.usersvc.service.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.xyz.usersvc.dto.RegisterCustomerRequest;
import org.xyz.usersvc.dto.CustomerResponse;
import org.xyz.usersvc.repository.AddressRepository;
import org.xyz.usersvc.repository.CustomerRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;

    @Override
    public CustomerResponse createCustomer(RegisterCustomerRequest registerCustomerRequest) {
        return null;
    }

    @Override
    public CustomerResponse getUserById(Long id) {
        return null;
    }

    @Override
    public List<CustomerResponse> getAllUsers() {
        return List.of();
    }

    @Override
    public CustomerResponse updateUser(RegisterCustomerRequest registerCustomerRequest) {
        return null;
    }

    @Override
    public String deleteUserById(Long id) {
        return "";
    }
}
