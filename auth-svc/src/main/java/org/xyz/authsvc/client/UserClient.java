package org.xyz.authsvc.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.xyz.authsvc.client.dto.AuthCustomerLoginReq;
import org.xyz.authsvc.client.dto.AuthCustomerLoginResp;
import org.xyz.authsvc.client.dto.CustomerSignupReq;
import org.xyz.authsvc.client.dto.UserResp;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url = "http://localhost:8083")
public interface UserClient {

//    @GetExchange("/{id}")
//    UserResp getUserById(@PathVariable("id") Long id);
//
//    @GetExchange("/email-exists")
//    boolean emailExists(@RequestParam String email);

    @PostMapping()
    void createCustomer(@RequestBody CustomerSignupReq signupReq);

//    UserResp getUserByEmail(@RequestParam() String email);

    @PostMapping("/api/v1/customers/login")
    AuthCustomerLoginResp getAuthLoginInfo(@RequestBody AuthCustomerLoginReq authCustomerLoginReq);

}
