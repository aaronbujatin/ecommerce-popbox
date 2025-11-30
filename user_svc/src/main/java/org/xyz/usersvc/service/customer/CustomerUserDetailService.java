package org.xyz.usersvc.service.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.xyz.usersvc.entity.Customer;
import org.xyz.usersvc.repository.CustomerRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Customer> customer = customerRepository.findByEmail(username);

        return customer.map(CustomerUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("customer email not found"));
    }
}
