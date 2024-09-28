package com.springsec.demo.service.impl;

import com.springsec.demo.entity.User;
import com.springsec.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Load user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        GrantedAuthority authority = user::getRole;

        // Convert User entity to Spring Security's UserDetails object
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail()) // Set email as the username
                .password(user.getPassword())  // Set the encoded password
                .authorities(authority)        // Set the user's role
                .build();
    }
}
