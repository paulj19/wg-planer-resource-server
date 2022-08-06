package com.wgplaner.registration;


import com.wgplaner.entity.User;
import com.wgplaner.repository.UserRepository;
import javax.validation.Valid;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<?> processUserRegistration(@Valid RegistrationDto registrationDto,
      Errors errors) {
    try {
      if (errors.hasErrors()) {
        log.warn("validation failed for registrationDto. " +  errors.getAllErrors());
        return ResponseEntity.unprocessableEntity().body(errors.getAllErrors());
      }
      User user = userRepository.save(registrationDto.mapToUser(passwordEncoder));
      log.info("new user registered and saved to DB. User Id " + user.getId());
      return ResponseEntity.ok(user);
    } catch (ValidationException e) {
      log.warn("new user registration validation failed during DB save ", e);
      return ResponseEntity.unprocessableEntity().body("validation failed during entity save");
    }
  }
}
