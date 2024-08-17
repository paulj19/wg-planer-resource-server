package com.wgplaner.password_recovery;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    // UserProfile userProfile = new UserProfile("paulo", "diljosepaul@gmail.com",
    // 1L, AuthServer.HOME_BREW);
    // userRepository.save(userProfile);
    String code = generateCode();
    PasswordRecoveryEmailEntity savedEntity = passwordRecoveryEmailRepository
        .save(new PasswordRecoveryEmailEntity(userProfile, code));
    System.out.println("savedEntity" + savedEntity.getUserProfile() + savedEntity.getCode());
    SimpleMailMessage mailMsg = createMessage(userProfile, code);
    mailService.sendAsync(mailMsg, new PasswordRecoveryMailCallback(savedEntity, mailMsg, mailService));
    System.out.println("sent email");
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
      System.out.println("Password recovery email sent success");
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
}
