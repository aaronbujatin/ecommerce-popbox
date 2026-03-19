package org.xyz.authsvc.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xyz.authsvc.dto.AuthTokenResp;
import org.xyz.authsvc.dto.SignInReq;
import org.xyz.authsvc.dto.SignInTokenResp;
import org.xyz.authsvc.dto.SignupReq;
//import org.xyz.authsvc.service.NotificationService;
import org.xyz.authsvc.service.impl.AuthenticationServiceImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

//    private final NotificationService notificationService;
    private final AuthenticationServiceImpl authenticationCustomerService;

//    @PostMapping("/otp/send")
//    public String sendOtp(@RequestParam String recipient) {
//        var otp = notificationService.sendOtp(recipient);
//        return "Otp " + otp;
//    }

//    @PostMapping("/otp/verify")
//    public AuthTokenResp verifyOtp(@RequestParam String recipient,
//                                   @RequestParam String otp) {
//
//       return notificationService.verifyOtp(recipient, otp);
//    }


    @PostMapping("/signup")
    public ResponseEntity<String> signupCustomer(@Valid @RequestBody SignupReq signupReq) throws BadRequestException, JsonProcessingException {
        authenticationCustomerService.signup(signupReq);

        return ResponseEntity.ok("User saved");
    }

    @PostMapping("/signin")
    public ResponseEntity<SignInTokenResp> loginAuthenticate(@Valid @RequestBody SignInReq signInReq) {
        return ResponseEntity.ok(authenticationCustomerService.signIn(signInReq));
    }


}
