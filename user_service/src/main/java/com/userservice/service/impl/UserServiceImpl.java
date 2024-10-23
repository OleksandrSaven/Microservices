package com.userservice.service.impl;

import com.userservice.client.ShoppingCartClient;
import com.userservice.domain.Role;
import com.userservice.domain.RoleName;
import com.userservice.domain.User;
import com.userservice.dto.CreateUserRequestDto;
import com.userservice.dto.UserDto;
import com.userservice.exception.EntityNotFoundException;
import com.userservice.exception.RegistrationException;
import com.userservice.mapper.UserMapper;
import com.userservice.repository.RoleRepository;
import com.userservice.repository.UserRepository;
import com.userservice.service.UserService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ShoppingCartClient shoppingCartClient;

    @Override
    public UserDto registration(CreateUserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Current email exist in the database");
        }
        User user = userMapper.toModel(requestDto);
        Set<Role> roles = new HashSet<>();
        Role defaultRole = roleRepository.findByName(RoleName.CUSTOMER).orElseThrow(
                () -> new EntityNotFoundException("Can't find role: " + RoleName.CUSTOMER));
        roles.add(defaultRole);
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User userDb = userRepository.save(user);
        shoppingCartClient.createShoppingCart(userDb.getId());
        return userMapper.toDto(userDb);
    }

    @Override
    public Long getUserId(String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("Can't find user with email: " + userEmail);
        }
        return user.get().getId();
    }
}
