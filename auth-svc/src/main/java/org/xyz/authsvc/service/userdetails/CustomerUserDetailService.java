package org.xyz.authsvc.service.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xyz.authsvc.client.UserClient;
import org.xyz.authsvc.client.dto.AuthCustomerLoginReq;

@RequiredArgsConstructor
@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        var customer = userClient.getAuthLoginInfo(new AuthCustomerLoginReq(
//                        signInReq.email(),
//                        signInReq.password()
//                )
//        ));

        return new CustomerUserDetails(
                null,
                null,
                null
        );
    }
}

