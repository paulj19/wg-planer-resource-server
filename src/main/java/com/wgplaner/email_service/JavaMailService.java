package com.wgplaner.email_service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class JavaMailService implements MailService {
    private final JavaMailSender javaMailSender;

    @Override
    public void sendAsync(SimpleMailMessage message, MailSentCallback mailSentCallback) {
        CompletableFuture.supplyAsync(() -> {
            javaMailSender.send(message);
            return null;
        }).handle((res, ex) -> {
            if (ex != null) {
                mailSentCallback.onFailure();
            } else {
                mailSentCallback.onSuccess();
            }
            return res;
        });
    }
}
