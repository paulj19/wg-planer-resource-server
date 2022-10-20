package com.wgplaner.email_service.email_verification.entity;

import com.wgplaner.core.entity.AbstractEntity;
import com.wgplaner.core.entity.User;
import com.wgplaner.email_service.email_verification.EmailVerificationStatus;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "email_verification_state")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class EmailVerificationState extends AbstractEntity {
    @OneToOne
    @JoinColumn(name = "user_id")
    private final User user;

    @Column(name = "uuid", unique = true)
    private final UUID uuid = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private @Setter EmailVerificationStatus status;

    public EmailVerificationState(User user) {
        this.user = user;
        this.status = EmailVerificationStatus.NOT_SENT;
    }
}
