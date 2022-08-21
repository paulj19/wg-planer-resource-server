package com.wgplaner.registration;


import com.wgplaner.entity.User;
import com.wgplaner.repository.UserRepository;
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
@RequestMapping("/register")
public class RegistrationController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Valid
    public ResponseEntity<?> processUserRegistration(@RequestBody @Valid RegistrationDto registrationDto) {
//        if (errors.hasErrors()) {
//            log.warn("validation failed for registrationDto. " + errors.getAllErrors());
//            return ResponseEntity.unprocessableEntity().body(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
//        }
        User user = userRepository.save(registrationDto.mapToUser(passwordEncoder));
        log.info("new user registered and saved to DB. User Id " + user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
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
       log.error("validation failed for registrationDto. " + errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(errors.toString());
    }
}
