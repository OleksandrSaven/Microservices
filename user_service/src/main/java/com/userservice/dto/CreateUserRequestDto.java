package com.userservice.dto;

import com.userservice.logger.Sensitive;
import com.userservice.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@FieldMatch(first = "password", second = "repeatPassword", message = "Passwords do not match")
public class CreateUserRequestDto {
    @Email
    private String email;
    @Sensitive
    @Size(min = 6)
    private String password;
    @Sensitive
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
