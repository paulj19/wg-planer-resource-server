package com.wgplaner.email_service.email_verification;


import com.wgplaner.CommonTestData;
import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import org.junit.jupiter.api.Test;
import org.springframework.mail.SimpleMailMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailVerificationMailMessageTest {

    @Test
    public void whenPassedCorrectState_shouldReturnCorrectMailMessage() {
        EmailVerificationState state = new EmailVerificationState(CommonTestData.createUser());
        SimpleMailMessage message = EmailVerificationMailMessage.generateMessage(state, "x.com");
        assertThat(message.getFrom()).isEqualTo("no-reply@x.com");
        assertThat(Objects.requireNonNull(message.getTo())[0]).isEqualTo(state.getUser().getEmail());
        assertThat(message.getSubject()).isEqualTo(EmailVerificationMailMessage.SUBJECT);
        assertThat(message.getText()).isEqualTo(String.format(EmailVerificationMailMessage.TEXT, "https://x.com/registration/email" +
                "-verification?" + URLEncoder.encode("id=" + state.getUuid() + "&email=abc@mail.com" , StandardCharsets.UTF_8)));
    }
}