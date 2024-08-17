package com.wgplaner.password_recovery;

import com.wgplaner.core.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;


public interface PasswordRecoveryEmailRepository extends CrudRepository<PasswordRecoveryEmailEntity, Long> {
  PasswordRecoveryEmailEntity findByCode(String code);
  PasswordRecoveryEmailEntity findByUserProfile(UserProfile userProfile);
}
