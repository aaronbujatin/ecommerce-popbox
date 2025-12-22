package org.xyz.usersvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xyz.usersvc.dto.CustomerInfoResp;
import org.xyz.usersvc.dto.LoginCustomerRequest;
import org.xyz.usersvc.dto.LoginTokenResponse;
import org.xyz.usersvc.dto.RegisterCustomerRequest;
import org.xyz.usersvc.exception.ResourceNotFoundException;
import org.xyz.usersvc.repository.CustomerRepository;
import org.xyz.usersvc.service.auth.AuthenticationCustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {


    private final CustomerRepository customerRepository;
    private final AuthenticationCustomerService authenticationCustomerService;

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterCustomerRequest registerCustomerRequest) throws BadRequestException, JsonProcessingException {
        authenticationCustomerService.signup(registerCustomerRequest);

        return ResponseEntity.ok("User saved");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> loginAuthenticate(@Valid @RequestBody LoginCustomerRequest loginCustomerRequest) {
        return ResponseEntity.ok(authenticationCustomerService.loginAuthToken(loginCustomerRequest));
    }


}
