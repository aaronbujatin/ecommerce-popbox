package org.xyz.usersvc.service.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.xyz.usersvc.dto.*;
import org.xyz.usersvc.entity.Customer;
import org.xyz.usersvc.entity.Role;
import org.xyz.usersvc.exception.ResourceNotFoundException;
import org.xyz.usersvc.exception.UserServiceException;
import org.xyz.usersvc.repository.AddressRepository;
import org.xyz.usersvc.repository.CustomerRepository;
import org.xyz.usersvc.repository.RoleRepository;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createCustomer(CustomerSignupReq customerSignupReq) {

        if (customerRepository.existsByEmail(customerSignupReq.email())) {
            throw new UserServiceException("email already exists");
        }

        var customer = Customer.builder()
                .email(customerSignupReq.email())
                .password(passwordEncoder.encode(customerSignupReq.password()))
                .firstName(customerSignupReq.firstName())
                .lastName(customerSignupReq.lastName())
                .phone(customerSignupReq.phone())
                .requestStringify(customerSignupReq.stringifyReq())
                .roles(defaultCustomerRoles())
                .build();

        customerRepository.save(customer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
//        return customerRepository.findById(id)
//                .orElseThrow(() -> new UserServiceException("customer not found"));
        return null;
    }

    @Override
    public CustomerResponse getAuthCustomerByEmail(String email) {


        var authCustomerInfo = customerRepository.findAuthCustomerInfoByEmail(email)
                .orElseThrow();


        return new CustomerResponse(
                1L,
                authCustomerInfo.getEmail(),
                authCustomerInfo.getPassword(),
                null, null, null, null, false, authCustomerInfo.getRoles()
        );
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

    @Override
    public CustomerResponse getAuthLoginInfo(LoginCustomerRequest customerRequest) {

        var authCustomerInfo = customerRepository.findAuthCustomerInfoByEmail(customerRequest.email())
                .orElseThrow();

        if (!passwordEncoder.matches(customerRequest.password(), authCustomerInfo.getPassword())) {
            throw new UserServiceException("User email or password does not match");
        }

        return new CustomerResponse(
                1L,
                authCustomerInfo.getEmail(),
                null,  null, null, null, null, false, authCustomerInfo.getRoles()
        );
    }

    private Set<Role> defaultCustomerRoles() {
        Long ROLE_USER_ID = 2L;
        Role role = roleRepository.findById(ROLE_USER_ID)
                .orElseThrow(ResourceNotFoundException::new);

        return Set.of(role);
    }
}
