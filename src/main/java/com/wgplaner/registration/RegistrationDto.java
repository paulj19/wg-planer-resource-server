package com.wgplaner.registration;

import com.wgplaner.common.validation.email.ValidEmail;
import com.wgplaner.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public record RegistrationDto(@NotBlank String username,
                              @NotNull @NotBlank @ValidEmail String email,
                              @NotBlank String password, @NotBlank String confirmPassword) {

    public User mapToUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), email);
    }
}
