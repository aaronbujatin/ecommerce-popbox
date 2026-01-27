package org.xyz.authsvc.service;


import org.xyz.authsvc.dto.AuthTokenResp;

public interface NotificationService {

    String sendOtp(String recipient);
    AuthTokenResp verifyOtp(String recipient, String otp);

}
