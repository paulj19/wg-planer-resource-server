package com.wgplaner.email_service.email_verification;

import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import org.springframework.mail.SimpleMailMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class EmailVerificationMailMessage {
    private static final String VERIFICATION_URL_FORMAT = "https://%s/registration/email-verification?";
    private static final String VERIFICATION_URL_FORMAT_QUERY = "id=%s&email=%s";
    public static final String SUBJECT ="Please validate your email";
    public static final String FROM = "no-reply@%s";
    public static final String TEXT = "please click on the link to verify your email: %s";

    public static SimpleMailMessage generateMessage(EmailVerificationState verificationState, String domainName) {
        Objects.requireNonNull(verificationState);
        Objects.requireNonNull(domainName);
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(String.format(FROM, domainName));
        message.setTo(verificationState.getUser().getEmail());
        message.setSubject(SUBJECT);
        message.setText(String.format(TEXT, generateVerificationLink(verificationState, domainName)));
        return message;
    }
    private static String generateVerificationLink(EmailVerificationState verificationState, String domainName){
        String url = String.format(VERIFICATION_URL_FORMAT, domainName);
        String urlQuery = URLEncoder.encode(String.format(VERIFICATION_URL_FORMAT_QUERY, verificationState.getUuid(), verificationState.getUser().getEmail()),
                StandardCharsets.UTF_8);
        return url + urlQuery;
    }
}
