package com.wgplaner.email_service.email_verification.controller;

import com.wgplaner.email_service.email_verification.EmailVerificationManager;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RestController
@Timed
@RequiredArgsConstructor
@RequestMapping("/email-verification")
public class EmailVerificationController {
    private final EmailVerificationManager emailVerificationManager;

    @GetMapping
    public void acceptVerification(@RequestParam UUID id, @RequestParam String email) {
        emailVerificationManager.verifyVerificationRequest(id, URLDecoder.decode(email, StandardCharsets.UTF_8));
    }
}
