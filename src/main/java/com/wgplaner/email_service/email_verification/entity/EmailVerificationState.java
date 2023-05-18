package com.wgplaner.email_service.email_verification.entity;

import com.wgplaner.core.entity.AbstractEntity;
import com.wgplaner.core.entity.UserProfile;
import com.wgplaner.email_service.email_verification.EmailVerificationStatus;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "email_verification_state")
@EqualsAndHashCode(callSuper = true)
public class EmailVerificationState extends AbstractEntity {
    @OneToOne
    @JoinColumn(name = "id")
    private UserProfile userProfile;

    @Column(name = "uuid", unique = true)
    @Type(type = "uuid-char")
    private final UUID uuid = UUID.randomUUID();

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private @Setter EmailVerificationStatus status;

    public EmailVerificationState(UserProfile userProfile) {
        this.userProfile = userProfile;
        this.status = EmailVerificationStatus.NOT_SENT;
    }

    public EmailVerificationState() {}
}
