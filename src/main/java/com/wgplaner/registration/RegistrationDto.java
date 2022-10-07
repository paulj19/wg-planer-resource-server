package com.wgplaner.registration;

import com.wgplaner.common.validation.ValidEmail;
import com.wgplaner.common.validation.ValidPassword;
import com.wgplaner.common.validation.ValidUsername;
import com.wgplaner.core.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public record RegistrationDto(
        @ValidUsername String username,
        @ValidEmail String email,
        @ValidPassword String password,
        @ValidPassword String confirmPassword) {

    public User mapToUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), email);
    }
}
