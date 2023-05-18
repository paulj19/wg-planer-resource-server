package com.wgplaner.email_service.email_verification;

public enum EmailVerificationStatus {
    NOT_SENT, //set when creating entry
    NOT_VERIFIED, //set when verification email is sent successfully
    VERIFIED, //set when call gets to the verify controller
    EXPIRED //set when controller gets called after expired time, a new verification message might be sent
}
