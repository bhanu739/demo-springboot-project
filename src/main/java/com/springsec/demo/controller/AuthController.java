package com.springsec.demo.controller;

import com.springsec.demo.dto.LoginRequest;
import com.springsec.demo.dto.LoginResponse;
import com.springsec.demo.exception.Error;
import com.springsec.demo.service.impl.UserDetailsServiceImpl;
import com.springsec.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Generate JWT token
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String token = jwtUtil.generateToken(userDetails.getUsername(), role);

            LoginResponse loginResponse = new LoginResponse(userDetails.getUsername(), loginRequest.getEmail(), role, token);
            return ResponseEntity.ok(loginResponse);

        } catch (AuthenticationException e) {
            Error errorResponse = Error.builder()
                    .timestamp(new Date())
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .message("Invalid credentials")
                    .path("/api/users/login")
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}