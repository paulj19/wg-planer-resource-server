package com.wgplaner.email_service.email_verification.repository;

import com.wgplaner.email_service.email_verification.entity.EmailVerificationState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface EmailVerificationMessageRepository extends CrudRepository<EmailVerificationState, Long> {
    @Query("SELECT e FROM EmailVerificationState e where e.user.id = :userId")
    EmailVerificationState findByUserId(@Param("userId") Long userId);
}
