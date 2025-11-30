package org.xyz.usersvc.service.customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;



public class CustomerUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final List<String> roles;


    public CustomerUserDetails(User user) {
        email = user.getUsername(),
        password = user.getPassword(),
        roles = user.getAuthorities()
                .stream().map()
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
