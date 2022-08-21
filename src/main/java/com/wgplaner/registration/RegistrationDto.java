package com.wgplaner.registration;

import com.wgplaner.common.validation.ValidEmail;
import com.wgplaner.common.validation.ValidPassword;
import com.wgplaner.common.validation.ValidUsername;
import com.wgplaner.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.AssertTrue;
import java.util.Objects;

public record RegistrationDto(
        @ValidUsername String username,
        @ValidEmail String email,
        @ValidPassword String password,
        @ValidPassword String confirmPassword) {

    public User mapToUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password), email);
    }

    @AssertTrue(message = "The password fields must match")
    private boolean isValidPass() {
        return Objects.equals(password, confirmPassword);
    }
}
