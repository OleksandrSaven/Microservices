package com.userservice.service;

import com.userservice.dto.CreateUserRequestDto;
import com.userservice.dto.UserDto;

public interface UserService {

    UserDto registration(CreateUserRequestDto requestDto);

    Long getUserId(String userName);

}
