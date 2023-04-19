package com.wgplaner.core.repository;

import com.wgplaner.core.AuthServer;
import com.wgplaner.core.entity.UserProfile;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserProfile, Long> {
    UserProfile findByUsername(String username);
    UserProfile findByEmail(String email);
    UserProfile findByOidAndAuthServer(long oid, AuthServer authServer);
}
