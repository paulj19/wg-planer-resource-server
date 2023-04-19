package com.wgplaner.email_service.email_verification;

import com.wgplaner.BaseIT;
import com.wgplaner.core.entity.UserProfile;
import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import com.wgplaner.email_service.email_verification.repository.EmailVerificationStateRepository;
import com.wgplaner.core.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


@Disabled
public class EmailVerificationManagerIT extends BaseIT {
    @Autowired
    private EmailVerificationManager emailVerificationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailVerificationStateRepository verificationRepository;


    @Disabled
    @Test
    public void whenPassedExistingEmail_shouldSendMailAndShouldChangeState() throws InterruptedException {
        UserProfile userProfile = getTestUser();
        emailVerificationManager.sentVerificationMessage(userProfile);
        Thread.sleep(7000);
        EmailVerificationState state = verificationRepository.findByUserId(userProfile.getId());
        assertThat(state.getStatus()).isEqualTo(EmailVerificationStatus.NOT_VERIFIED);
    }

    @Test
    @Disabled
    public void whenPassedNonExisting_shouldNotSendMailAndShouldChangeState() throws InterruptedException {
//        UserProfile userProfile = new UserProfile("test", "test", "test@x123xxxxtest.com");
//        userRepository.save(userProfile);
//        emailVerificationManager.sentVerificationMessage(userProfile);
//        Thread.sleep(8000);
//        EmailVerificationState state = verificationRepository.findByUserId(userProfile.getId());
//        assertThat(state.getStatus()).isEqualTo(EmailVerificationStatus.NOT_SENT);
    }
}