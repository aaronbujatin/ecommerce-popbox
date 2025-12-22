package org.xyz.usersvc.service.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.xyz.usersvc.dto.LoginCustomerRequest;
import org.xyz.usersvc.dto.LoginTokenResponse;
import org.xyz.usersvc.dto.RegisterCustomerRequest;
import org.xyz.usersvc.entity.Customer;
import org.xyz.usersvc.entity.Role;
import org.xyz.usersvc.exception.ResourceNotFoundException;
import org.xyz.usersvc.repository.CustomerRepository;
import org.xyz.usersvc.repository.RoleRepository;
import org.xyz.usersvc.service.customer.CustomerUserDetails;
import org.xyz.usersvc.service.jwt.JwtService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationCustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final RoleRepository roleRepository;

    public void signup(RegisterCustomerRequest registerCustomerRequest) throws BadRequestException, JsonProcessingException {

        if (customerRepository.existsByEmail(registerCustomerRequest.email())) {
            throw new BadRequestException(String.format("email %s already exist", registerCustomerRequest.email()));
        }

        Customer customer = Customer.builder()
                .email(registerCustomerRequest.email())
                .password(passwordEncoder.encode(registerCustomerRequest.password()))
                .firstName(registerCustomerRequest.firstName())
                .lastName(registerCustomerRequest.lastName())
                .phone(registerCustomerRequest.phone())
                .requestStringify(objectMapper.writeValueAsString(registerCustomerRequest))
                .roles(defaultCustomerRoles())
                .build();

        customerRepository.save(customer);
    }

    public LoginTokenResponse loginAuthToken(LoginCustomerRequest loginCustomerRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCustomerRequest.email(),
                            loginCustomerRequest.password()
                    )
            );

            CustomerUserDetails customer = (CustomerUserDetails) auth.getPrincipal();

            String token = jwtService.generateToken(customer);
            return new LoginTokenResponse(token, jwtService.getJwtExpirationTime());
        } catch (Exception e) {
            throw new RuntimeException("Auth failed: " + e.getMessage());
        }
    }


    private Set<Role> defaultCustomerRoles() {
        Long ROLE_USER_ID = 2L;
        Role role = roleRepository.findById(ROLE_USER_ID)
                .orElseThrow(ResourceNotFoundException::new);

        return Set.of(role);
    }

}
