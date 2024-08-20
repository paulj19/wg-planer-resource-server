package com.wgplaner.password_recovery;

import com.wgplaner.core.entity.AbstractEntity;
import com.wgplaner.core.entity.UserProfile;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "password_recovery")
@EqualsAndHashCode(callSuper = true)
public class PasswordRecoveryEmailEntity extends AbstractEntity {
  @ManyToOne
  @JoinColumn(name = "user_profile_id", referencedColumnName = "id")
  private UserProfile userProfile;

  @Column(name = "code", unique = true, nullable = false)
  private String code;

  public PasswordRecoveryEmailEntity(UserProfile userProfile, String code) {
    this.userProfile = userProfile;
    this.code = code;
  }

  public PasswordRecoveryEmailEntity() {
  }
}
