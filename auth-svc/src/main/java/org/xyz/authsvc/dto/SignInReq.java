package org.xyz.authsvc.dto;

public record SignInReq (
        String email,
        String password
) {

}
