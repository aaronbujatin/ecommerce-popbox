package org.xyz.authsvc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.xyz.authsvc.client.UserClient;
import org.xyz.authsvc.client.dto.AuthCustomerLoginReq;
import org.xyz.authsvc.client.dto.CustomerSignupReq;
import org.xyz.authsvc.dto.SignInTokenResp;
import org.xyz.authsvc.dto.SignupReq;
import org.xyz.authsvc.dto.SignInReq;
import org.xyz.authsvc.service.jwt.JwtService;
import org.xyz.authsvc.service.userdetails.CustomerUserDetails;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl {

//    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserClient userClient;

    public void signup(SignupReq signupReq) throws JsonProcessingException {

        var customer = new CustomerSignupReq(
                signupReq.email(),
                signupReq.password(),
                signupReq.firstName(),
                signupReq.lastName(),
                signupReq.phone(),
                objectMapper.writeValueAsString(signupReq)
            );

        userClient.createCustomer(customer);

    }

    public SignInTokenResp signIn(SignInReq signInReq) {
        System.out.println(userClient.getClass());
        try {
//                Authentication auth = authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                signInReq.email(),
//                                signInReq.password()
//                            )
//                );

            var authCustomerInfo = userClient.getAuthLoginInfo(
                    new AuthCustomerLoginReq(
                            signInReq.email(),
                            signInReq.password()
                    )
            );

            CustomerUserDetails customer = new CustomerUserDetails(
                    authCustomerInfo.email(), null, authCustomerInfo.roles()
            );

            String token = jwtService.generateToken(customer);
            return new SignInTokenResp(token, jwtService.getJwtExpirationTime());
        } catch (Exception e) {
            throw new RuntimeException("Auth failed: " + e.getMessage());
        }
    }


//    private Set<String> defaultCustomerRoles() {
//        Long ROLE_USER_ID = 2L;
//        Role role = roleRepository.findById(ROLE_USER_ID)
//                .orElseThrow(ResourceNotFoundException::new);
//
//        return Set.of(role);
//    }
}
