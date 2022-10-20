package com.wgplaner.email_service.email_verification;

import com.wgplaner.core.entity.User;
import com.wgplaner.email_service.MailSentCallback;
import com.wgplaner.email_service.MailService;
import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import com.wgplaner.email_service.email_verification.repository.EmailVerificationStateRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailVerificationManager implements MailSentCallback {
    private final MailService mailService;
    private User user;
    private final EmailVerificationStateRepository verificationRepository;
    private final MeterRegistry meterRegistry;

    @Value("${email-verification.domain-name}")
    private String domainName;
    private EmailVerificationState verificationState;

    public void sentVerificationMessage(User user) {
        //TODO domain should be pinged AND ip address of email should be retrieved before sending mail to avoid bounces.
        this.user = user;
        verificationState = verificationRepository.save(new EmailVerificationState(user));
        SimpleMailMessage message = EmailVerificationMailMessage.generateMessage(verificationState, domainName);
        mailService.sendAsync(message, this);
    }

    public void verifyVerificationRequest(UUID uuid, String email) {
        EmailVerificationState state = verificationRepository.findByUuid(uuid);
        if (state != null && state.getUser().getEmail().equals(email) && state.getStatus().equals(EmailVerificationStatus.NOT_VERIFIED)) {
            state.setStatus(EmailVerificationStatus.VERIFIED);
            verificationRepository.save(state);
            log.info("email_verification: email {} of user id {} verified ", email, state.getUser().getId());
            meterRegistry.counter("email.verification.success", "email_verification_success").increment();
        } else {
            if(state != null) {
                log.warn("email_verification: email {} uuid {} pair does not match for user id {}", email, uuid, state.getUser().getId());
            } else {
                log.warn("email_verification: email {} uuid {} failed bc no state found with this uuid", email, uuid);
            }
            meterRegistry.counter("email.verification.failure", "email_verification_failure", "possible_attack").increment();
        }
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
