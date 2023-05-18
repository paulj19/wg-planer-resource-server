package com.wgplaner.email_service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class MailConfig {
    @Bean
    public MailService mailService(JavaMailSender javaMailSender) {
        return new JavaMailService(javaMailSender);
    }
}
