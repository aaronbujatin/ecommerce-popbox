package org.xyz.authsvc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.xyz.authsvc.dto.*;

public interface AuthService {

    void signupCustomer(SignupReq signupReq) throws JsonProcessingException;
    SignInTokenResp signInCustomer(SignInReq signInReq);
    AuthTokenResp refreshToken(RefreshTokenReq refreshTokenReq);

}
