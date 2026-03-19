package org.xyz.authsvc.service;

import org.xyz.authsvc.dto.SignupReq;

public interface AuthService {



    void signupCustomer(SignupReq signupReq);
    void signInCustomer();
}
