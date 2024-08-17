package com.wgplaner.password_recovery;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.wgplaner.core.AuthServer;
import com.wgplaner.core.entity.UserProfile;
import com.wgplaner.core.repository.UserRepository;
import com.wgplaner.email_service.MailSentCallback;
import com.wgplaner.email_service.MailService;
import com.wgplaner.registration.UserProfileDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/password-recovery")
@RequiredArgsConstructor
public class PasswordRecoveryController {
  private final UserRepository userRepository;
  private final PasswordRecoveryEmailRepository passwordRecoveryEmailRepository;
  private final MailService mailService;
  private static final Random RANDOM = new Random();

  @PostMapping(path = "/initiate")
  public void initiateEmail(@RequestParam(name = "email", required = true) String email) {
    UserProfile userProfile = userRepository.findByEmail(email);
    if (userProfile == null) {
      throw new EmailNotFoundException("Email not found: " + email);
    }
    String code = generateCode();
    PasswordRecoveryEmailEntity savedEntity = passwordRecoveryEmailRepository
        .save(new PasswordRecoveryEmailEntity(userProfile, code));
    SimpleMailMessage mailMsg = createMessage(userProfile, code);
    mailService.sendAsync(mailMsg, new PasswordRecoveryMailCallback(savedEntity, mailMsg, mailService));
  }

  @GetMapping(path = "/validate")
  public UserProfileDto validateCode(@RequestParam(name = "code", required = true) String code) {
    PasswordRecoveryEmailEntity entity = passwordRecoveryEmailRepository.findByCode(code);
    if (entity == null) {
      throw new CodeNotFoundException("Code not found: " + code);
    }
    if (entity.getCreationDate().plusMinutes(20).isBefore(ZonedDateTime.now())) {
      throw new CodeExpiredException("Code expired: " + code);
    }
    UserProfile userProfile = entity.getUserProfile();
    return new UserProfileDto(userProfile.getId(), userProfile.getUsername(), userProfile.getEmail(),
        userProfile.getOid(), userProfile.getAuthServer());
  }

  private static String generateCode() {
    String samples = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder code = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      int index = RANDOM.nextInt(samples.length());
      code.append(samples.charAt(index));
    }
    return code.toString();
  }

  private static SimpleMailMessage createMessage(UserProfile userProfile, String code) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(userProfile.getEmail());
    message.setSubject("Password Recovery");
    message.setText("Your password recovery code is: " + code);
    return message;
  }

  public class PasswordRecoveryMailCallback implements MailSentCallback {
    private final PasswordRecoveryEmailEntity entity;
    private final SimpleMailMessage message;
    private final MailService mailService;
    private int retry = 0;

    public PasswordRecoveryMailCallback(PasswordRecoveryEmailEntity entity, SimpleMailMessage message,
        MailService mailService) {
      this.entity = entity;
      this.message = message;
      this.mailService = mailService;
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure() {
      log.error(
          "Failed to send password recovery email entity: " + entity + " retry attempt:" + retry);
      if (retry <= 3) {
        mailService.sendAsync(message, this);
        retry++;
      }
    }
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(EmailNotFoundException.class)
  public ResponseEntity<String> handleValidationExceptions(EmailNotFoundException ex) {
    log.error("PasswordRecovery initiate error" + ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(CodeNotFoundException.class)
  public ResponseEntity<String> handleValidationExceptions(CodeNotFoundException ex) {
    log.error("PasswordRecovery code not found" + ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
  }

  @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
  @ExceptionHandler(CodeExpiredException.class)
  public ResponseEntity<String> handleValidationExceptions(CodeExpiredException ex) {
    log.error("PasswordRecovery code expired" + ex.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
  }
}
