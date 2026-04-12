package org.xyz.authsvc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.xyz.authsvc.client.UserClient;
import org.xyz.authsvc.client.dto.AuthCustomerLoginReq;
import org.xyz.authsvc.client.dto.CustomerSignupReq;
import org.xyz.authsvc.dto.*;
import org.xyz.authsvc.entity.RefreshToken;
import org.xyz.authsvc.repository.RefreshTokenRepository;
import org.xyz.authsvc.service.AuthService;
import org.xyz.authsvc.service.jwt.JwtService;
import org.xyz.authsvc.service.userdetails.CustomerUserDetails;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final UserClient userClient;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${security.jwt.refresh-token-expiry-time}")
    private Long refreshTokenExpiryTime;

//    public void signup(SignupReq signupReq) throws JsonProcessingException {
//
//        var customer = new CustomerSignupReq(
//                signupReq.email(),
//                signupReq.password(),
//                signupReq.firstName(),
//                signupReq.lastName(),
//                signupReq.phone(),
//                objectMapper.writeValueAsString(signupReq)
//            );
//
//        userClient.createCustomer(customer);
//
//    }
//
//    public SignInTokenResp signIn(SignInReq signInReq) {
//        System.out.println(userClient.getClass());
//        try {
////                Authentication auth = authenticationManager.authenticate(
////                        new UsernamePasswordAuthenticationToken(
////                                signInReq.email(),
////                                signInReq.password()
////                            )
////                );
//
//            var authCustomerInfo = userClient.getAuthLoginInfo(
//                    new AuthCustomerLoginReq(
//                            signInReq.email(),
//                            signInReq.password()
//                    )
//            );
//
//            CustomerUserDetails customer = new CustomerUserDetails(
//                    authCustomerInfo.email(), null, authCustomerInfo.roles()
//            );
//
//            String token = jwtService.generateToken(customer);
//            return new SignInTokenResp(token, jwtService.getJwtExpirationTime(), null);
//        } catch (Exception e) {
//            throw new RuntimeException("Auth failed: " + e.getMessage());
//        }
//    }

    @Override
    public void signupCustomer(SignupReq signupReq) throws JsonProcessingException {
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

    @Override
    public SignInTokenResp signInCustomer(SignInReq signInReq) {
        try {
            var authCustomerInfo = userClient.getAuthLoginInfo(
                    new AuthCustomerLoginReq(
                            signInReq.email(),
                            signInReq.password()
                    )
            );

            CustomerUserDetails customer = new CustomerUserDetails(
                    authCustomerInfo.email(), null, authCustomerInfo.roles()
            );

            var tokenId = createRefreshToken(new RefreshTokenReq(authCustomerInfo.id(), null));

            String token = jwtService.generateToken(customer);
            return new SignInTokenResp(token, jwtService.getJwtExpirationTime(), tokenId);
        } catch (Exception e) {
            throw new RuntimeException("Auth failed: " + e.getMessage());
        }
    }

    @Override
    public AuthTokenResp refreshToken(RefreshTokenReq refreshTokenReq) {

        var refreshToken = refreshTokenRepository.findByTokenId(refreshTokenReq.tokenId())
                .orElseThrow(() -> new RuntimeException("token id not found"));

        var user = userClient.getUserById(refreshTokenReq.userId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        verifyRefreshToken(refreshToken);

        return generateRefreshToken(refreshTokenReq);
    }

    private RefreshToken verifyRefreshToken(RefreshToken refreshToken) {
        if (refreshToken.getExpiry().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("refresh token is expired. Please login again");
        }
        return refreshToken;
    }

    private String createRefreshToken(RefreshTokenReq refreshTokenReq) {
        var user = userClient.getUserById(refreshTokenReq.userId())
                .orElseThrow(() -> new RuntimeException("user not found"));

//        refreshTokenRepository.deleteByUserId(refreshTokenReq.userId());

        var refreshToken = RefreshToken.builder()
                .userId(user.id())
                .tokenId(UUID.randomUUID().toString())
                .expiry(Instant.now().plusMillis(refreshTokenExpiryTime))
                .build();

        return refreshTokenRepository.save(refreshToken).getTokenId();
    }

    private AuthTokenResp generateRefreshToken(RefreshTokenReq refreshTokenReq) {
        try {

            var user = userClient.getUserById(refreshTokenReq.userId())
                    .orElseThrow(() -> new RuntimeException("user not found"));

            var authCustomerInfo = userClient.getAuthLoginInfo(
                    new AuthCustomerLoginReq(
                            user.email(),
                            user.password()
                    )
            );

            CustomerUserDetails customer = new CustomerUserDetails(
                    authCustomerInfo.email(), null, authCustomerInfo.roles()
            );

            String token = jwtService.generateToken(customer);
            return new AuthTokenResp(token, jwtService.getJwtExpirationTime(), null, null);
        } catch (Exception e) {
            throw new RuntimeException("Auth failed: " + e.getMessage());
        }
    }



}
