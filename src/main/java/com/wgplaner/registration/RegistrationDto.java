package com.wgplaner.registration;

import com.wgplaner.common.validation.email.ValidEmail;
import com.wgplaner.entity.User;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class RegistrationDto {

    @NotBlank
    private String username;
    @NotNull
    @NotBlank
    @ValidEmail
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String confirmPassword;

    public User mapToUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), email);
    }
}
