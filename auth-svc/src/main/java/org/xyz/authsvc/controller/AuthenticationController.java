package org.xyz.authsvc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xyz.authsvc.dto.AuthTokenResp;
import org.xyz.authsvc.service.NotificationService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final NotificationService notificationService;

    @PostMapping("/otp/send")
    public String sendOtp(@RequestParam String recipient) {
        var otp = notificationService.sendOtp(recipient);
        return "Otp " + otp;
    }

    @PostMapping("/otp/verify")
    public AuthTokenResp verifyOtp(@RequestParam String recipient,
                                   @RequestParam String otp) {

       return notificationService.verifyOtp(recipient, otp);
    }


}
