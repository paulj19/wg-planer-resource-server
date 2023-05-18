package com.wgplaner.email_service.email_verification;

import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import org.springframework.mail.SimpleMailMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class EmailVerificationMailMessage {
    private static final String VERIFICATION_URL = "https://%s/email-verification?";
    private static final String VERIFICATION_URL_QUERY_ID = "id=%s";
    private static final String VERIFICATION_URL_QUERY_EMAIL = "email=%s";
    public static final String SUBJECT ="Please validate your email";
    public static final String FROM = "no-reply@%s";
    public static final String TEXT = "please click on the link to verify your email: %s";

    public static SimpleMailMessage generateMessage(EmailVerificationState verificationState, String domainName) {
        Objects.requireNonNull(verificationState);
        Objects.requireNonNull(domainName);
        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom(String.format(FROM, domainName));
        message.setTo(verificationState.getUserProfile().getEmail());
        message.setSubject(SUBJECT);
        message.setText(String.format(TEXT, generateVerificationLink(verificationState, domainName)));
        return message;
    }
    private static String generateVerificationLink(EmailVerificationState verificationState, String domainName){
        String url = String.format(VERIFICATION_URL, domainName);
        String urlQuery =
                String.format(VERIFICATION_URL_QUERY_ID, verificationState.getUuid()) + "&" + String.format(VERIFICATION_URL_QUERY_EMAIL,
                URLEncoder.encode(verificationState.getUserProfile().getEmail(), StandardCharsets.UTF_8));
        return url + urlQuery;
    }
}
