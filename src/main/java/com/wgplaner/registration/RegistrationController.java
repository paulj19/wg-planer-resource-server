package com.wgplaner.registration;

import com.wgplaner.auth.AuthServerRequester;
import com.wgplaner.core.entity.UserProfile;
import com.wgplaner.core.repository.UserRepository;
import io.micrometer.core.annotation.Timed;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Timed
@Validated
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final AuthServerRequester authServerRequester;

    public RegistrationController(UserRepository userRepository, AuthServerRequester authServerRequester) {
        this.userRepository = userRepository;
        this.authServerRequester = authServerRequester;
    }

    @Valid
    @PostMapping(path = "/new")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileDto registerNewUser(@RequestBody @Valid RegistrationDto registrationDto) {
        validateUsernameUnique(registrationDto);
        validateEmailUnique(registrationDto);
        Long oid = authServerRequester.registerUserAndFetchOid(registrationDto.username(), registrationDto.password(), registrationDto.floorId());
        UserProfile userProfile = userRepository.save(new UserProfile(registrationDto.username(), registrationDto.email(), registrationDto.floorId(), oid, registrationDto.authServer()));
        log.info("New user registered and saved to DB. User Id {}.", userProfile.getId());
        return new UserProfileDto(userProfile.getId(), userProfile.getUsername(), userProfile.getEmail(), userProfile.getFloorId(), userProfile.getOid(), userProfile.getAuthServer());
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

    private void validateUsernameUnique(RegistrationDto registrationDto) {
        if(userRepository.findByUsername(registrationDto.username()) != null ) {
            log.error("New user registration failed, non-unique username. Registration dto: {}", registrationDto);
            throw new ValidationException("username=non-unique username: " + registrationDto.username());
        }
    }

    private void validateEmailUnique(RegistrationDto registrationDto) {
        if(userRepository.findByEmail( registrationDto.email()) != null ) {
            log.error("New user registration failed, non-unique email. Registration dto: {}", registrationDto);
            throw new ValidationException("email=non-unique email: " + registrationDto.email());
        }
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
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleCustomValidationException(
            ValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(
            RuntimeException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
