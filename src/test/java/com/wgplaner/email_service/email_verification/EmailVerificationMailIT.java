package com.wgplaner.email_service.email_verification;

import com.wgplaner.BaseIT;
import com.wgplaner.CommonTestData;
import com.wgplaner.core.entity.User;
import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import com.wgplaner.email_service.email_verification.repository.EmailVerificationMessageRepository;
import com.wgplaner.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


public class EmailVerificationMailIT extends BaseIT {
    @Autowired
    private EmailVerificationMail emailVerificationMail;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailVerificationMessageRepository verificationRepository;


    @Disabled
    @Test
    public void whenPassedExistingEmail_shouldSendMailAndShouldChangeState() throws InterruptedException {
        User user = CommonTestData.createUser();
        userRepository.save(user);
        emailVerificationMail.sentValidationMessage(user);
        Thread.sleep(7000);
        EmailVerificationState state = verificationRepository.findByUserId(user.getId());
        assertThat(state.getStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
    }

    @Test
    @Disabled
    public void whenPassedNonExisting_shouldNotSendMailAndShouldChangeState() throws InterruptedException {
        User user = new User("test", "test", "test@x123xxxxtest.com");
        userRepository.save(user);
        emailVerificationMail.sentValidationMessage(user);
        Thread.sleep(8000);
        EmailVerificationState state = verificationRepository.findByUserId(user.getId());
        assertThat(state.getStatus()).isEqualTo(EmailVerificationStatus.NOT_SENT);
    }
}