package org.xyz.authsvc.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.xyz.authsvc.client.dto.UserResp;

@HttpExchange("/users")
public interface UserClient {

    @GetExchange("/{id}")
    UserResp getUserById(@PathVariable("id") Long id);

    UserResp getUserByEmail(@RequestParam() String email);
}
