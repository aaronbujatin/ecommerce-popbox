package org.xyz.usersvc.service.customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.xyz.usersvc.entity.Role;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


public record CustomerUserDetails(String email, String passwordHash, Set<Role> roles) implements UserDetails {


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
