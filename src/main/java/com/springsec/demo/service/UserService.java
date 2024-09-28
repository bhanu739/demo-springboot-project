package com.springsec.demo.service;

import com.springsec.demo.dto.RegisterUserDto;
import com.springsec.demo.dto.UserRegistrationResponse;

public interface UserService {
    UserRegistrationResponse registerUser(RegisterUserDto registerUserDto);
}
