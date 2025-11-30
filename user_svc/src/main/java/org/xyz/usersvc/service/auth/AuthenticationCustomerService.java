package org.xyz.usersvc.service.auth;

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
import org.xyz.usersvc.repository.CustomerRepository;
import org.xyz.usersvc.service.jwt.JwtService;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationCustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void signup(RegisterCustomerRequest registerCustomerRequest) throws BadRequestException {

        if (customerRepository.existsByEmail(registerCustomerRequest.email())) {
            throw new BadRequestException(String.format("email %s already exist", registerCustomerRequest.email()));
        }

        Customer customer = new Customer();
        customer.setEmail(registerCustomerRequest.email());
        customer.setPassword(passwordEncoder.encode(registerCustomerRequest.password()));
        customer.setFirstName(registerCustomerRequest.firstName());
        customer.setLastName(registerCustomerRequest.lastName());
        customer.setPhone(registerCustomerRequest.phone());

        customerRepository.save(customer);
    }

    public LoginTokenResponse loginAuthToken(LoginCustomerRequest loginCustomerRequest) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginCustomerRequest.email(),
                            loginCustomerRequest.password()
                    )
            );

            Customer customer = (Customer) auth.getPrincipal();

            String token = jwtService.generateToken(customer);
            return new LoginTokenResponse(token, jwtService.getJwtExpirationTime());
        } catch (Exception e) {
            throw new RuntimeException("Auth failed: " + e.getMessage());
        }
    }

}
