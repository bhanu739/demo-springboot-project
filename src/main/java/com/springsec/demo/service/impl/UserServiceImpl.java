package com.springsec.demo.service.impl;

import com.springsec.demo.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springsec.demo.dto.RegisterUserDto;
import com.springsec.demo.entity.User;
import com.springsec.demo.repository.UserRepository;
import com.springsec.demo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(RegisterUserDto registerUserDto) {
        if (userRepository.existsByUsername(registerUserDto.getUsername())) {
            throw new UserAlreadyExistsException("Username is already taken");
        }
        if (userRepository.existsByEmail(registerUserDto.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use");
        }

        User user = new User();
        user.setUsername(registerUserDto.getUsername());
        user.setEmail(registerUserDto.getEmail());

        // Hash the password
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        // Default role
        user.setRole("USER");

        return userRepository.save(user);
    }
}
