package com.wgplaner.email_service;

public interface MailSentCallback {
    void onSuccess();
    void onFailure();
}
