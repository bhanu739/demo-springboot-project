package com.springsec.demo.service;

import com.springsec.demo.dto.RegisterUserDto;
import com.springsec.demo.entity.User;

public interface UserService {
    User registerUser(RegisterUserDto registerUserDto);
}
