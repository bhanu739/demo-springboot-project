package com.springsec.demo.service.impl;

import com.springsec.demo.dto.UserRegistrationResponse;
import com.springsec.demo.exception.InvalidPasswordException;
import com.springsec.demo.exception.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springsec.demo.dto.RegisterUserDto;
import com.springsec.demo.entity.User;
import com.springsec.demo.repository.UserRepository;
import com.springsec.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserRegistrationResponse registerUser(RegisterUserDto registerUserDto) {
        logger.info("Attempting to register user: {}, Email: {}", registerUserDto.getUsername(), registerUserDto.getEmail());

        // Check if email or username already exists
        validateUserExistence(registerUserDto);

        // Validate password format
        validatePassword(registerUserDto.getPassword());

        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());

        // Hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        // Default role
        user.setRole("USER");

        userRepository.save(user);
        logger.info("User registered successfully: {}, Email: {}", user.getUsername(), user.getEmail());

        return new UserRegistrationResponse("User " + user.getUsername() + " registered successfully");
    }

    private void validateUserExistence(RegisterUserDto registerUserDto) {
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        if (userRepository.existsByUsername(registerUserDto.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
    }

    private void validatePassword(String password) {
        // Custom password validation
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        if (!password.matches(passwordRegex)) {
            throw new InvalidPasswordException("Password must be at least 8 characters long and contain at least one digit, one lowercase letter, one uppercase letter, and one special character.");
        }
    }
}
