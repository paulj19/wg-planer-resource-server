package com.wgplaner.email_service;

import org.springframework.mail.SimpleMailMessage;

public interface MailService {
    void sendAsync(SimpleMailMessage message, MailSentCallback mailSentCallback);
}
