package com.springsec.demo.controller;

import com.springsec.demo.dto.UserRegistrationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.springsec.demo.dto.RegisterUserDto;
import com.springsec.demo.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs related to user management")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user by providing necessary user details in the request body.")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody RegisterUserDto registerUserDto) {
        UserRegistrationResponse response = userService.registerUser(registerUserDto);
        return ResponseEntity.ok(response);
    }

}
