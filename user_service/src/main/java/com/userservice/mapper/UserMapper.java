package com.userservice.mapper;

import com.userservice.domain.User;
import com.userservice.dto.CreateUserRequestDto;
import com.userservice.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toModel(CreateUserRequestDto requestDto);
}
