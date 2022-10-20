package com.wgplaner.email_service.email_verification;

import com.wgplaner.core.entity.User;
import com.wgplaner.email_service.MailSentCallback;
import com.wgplaner.email_service.MailService;
import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import com.wgplaner.email_service.email_verification.repository.EmailVerificationMessageRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationMail implements MailSentCallback {
    private final MailService mailService;
    private User user;
    private final EmailVerificationMessageRepository verificationRepository;

    @Value("${email-verification.domain-name}")
    private String domainName;
    private EmailVerificationState verificationState;

    public void sentValidationMessage(User user) {
        //TODO domain should be pinged AND ip address of email should be retrieved before sending mail to avoid bounces.
        this.user = user;
        verificationState = verificationRepository.save(new EmailVerificationState(user));
        SimpleMailMessage message = EmailVerificationMailMessage.generateMessage(verificationState, domainName);
        mailService.sendAsync(message, this);
    }

    @Override
    public void onSuccess() {
        log.info("email verification mail sent successful for user id {}", user.getId());
        verificationState.setStatus(EmailVerificationStatus.NOT_VERIFIED);
        verificationRepository.save(verificationState);
    }

    @Override
    public void onFailure() {
        log.info("email verification mail sent failed for user id {}", user.getId());
        Metrics.counter("email.verification.mail.failure", "verification-mail").increment();
        //todo try again based on exceptions?
    }
}
