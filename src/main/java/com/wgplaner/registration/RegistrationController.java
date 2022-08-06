package com.wgplaner.registration;


import com.wgplaner.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.ValidationException;

@RestController
@RequestMapping("/register")
public class RegistrationController {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> processUserRegistration(@Valid RegistrationDto registrationDto, Errors errors) {
        try {
            if(errors.hasErrors()) {
                return ResponseEntity.unprocessableEntity().body(errors.getAllErrors());
            }
            return ResponseEntity.ok(userRepository.save(registrationDto.mapToUser(passwordEncoder)));
        } catch (ValidationException e) {
            return ResponseEntity.unprocessableEntity().body("validation failed during entity save");
        }
    }
}
