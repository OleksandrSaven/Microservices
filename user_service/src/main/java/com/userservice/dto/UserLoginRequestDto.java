package com.userservice.dto;

import com.userservice.logger.Sensitive;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserLoginRequestDto {
    @NotBlank
    @Email
    private String email;
    @Sensitive
    @NotBlank
    @Size(min = 4)
    private String password;
}
