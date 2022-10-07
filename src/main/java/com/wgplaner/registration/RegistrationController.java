package com.wgplaner.registration;


import com.wgplaner.core.entity.User;
import com.wgplaner.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Timed
@RequestMapping("/registration")
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/new")
    @Valid
    public ResponseEntity<?> processUserRegistration(@RequestBody @Valid RegistrationDto registrationDto) {
        if(userRepository.findByUsername(registrationDto.username()) != null || userRepository.findByEmail(registrationDto.email()) != null) {
            log.info("New user registration failed, non-unique username or email. Registration dto: {}", registrationDto);
            return ResponseEntity.unprocessableEntity().body("username and email must be unique");
        }
        User user = userRepository.save(registrationDto.mapToUser(passwordEncoder));
        log.info("New user registered and saved to DB. User Id {}.", user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(path = "/username-available")
    //todo validity checks
    public ResponseEntity<Boolean> isUserNameAvailable(@RequestParam String username) {
        return ResponseEntity.ok(userRepository.findByUsername(username) == null);
    }

    @GetMapping(path = "/email-available")
    //todo validity checks
    public ResponseEntity<Boolean> isEmailAvailable(@RequestParam String email) {
        return ResponseEntity.ok(userRepository.findByUsername(email) == null);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
       log.error("Validation failed for registrationDto. " + errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errors.toString());
    }
}
