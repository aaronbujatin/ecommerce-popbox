package org.xyz.usersvc.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xyz.usersvc.dto.LoginCustomerRequest;
import org.xyz.usersvc.dto.LoginTokenResponse;
import org.xyz.usersvc.dto.RegisterCustomerRequest;
import org.xyz.usersvc.service.auth.AuthenticationCustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationCustomerService authenticationCustomerService;


    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterCustomerRequest registerCustomerRequest) throws BadRequestException {
        authenticationCustomerService.signup(registerCustomerRequest);

        return ResponseEntity.ok("User saved");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginTokenResponse> loginAuthenticate(@RequestBody LoginCustomerRequest loginCustomerRequest) {
        System.out.println(loginCustomerRequest);
        return ResponseEntity.ok(authenticationCustomerService.loginAuthToken(loginCustomerRequest));


    }


}
