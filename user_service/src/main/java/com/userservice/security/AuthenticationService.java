package com.userservice.security;

import com.userservice.dto.UserLoginRequestDto;
import com.userservice.dto.UserLoginResponseDto;
import com.userservice.exception.RegistrationException;
import com.userservice.repository.UserRepository;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public UserLoginResponseDto generateToken(UserLoginRequestDto requestDto) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                        requestDto.getPassword()));
        if (authenticate.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authenticate.getAuthorities();
            Set<String> roles = authorities.stream()
                    .map(String::valueOf).collect(Collectors.toSet());
            String token = jwtUtil.generateToken(requestDto.getEmail(), roles);
            return new UserLoginResponseDto(token);
        } else {
            throw new RegistrationException("Invalid access");
        }
    }

    public boolean validateToken(String token) {
        return jwtUtil.isValidToken(token);
    }
}
