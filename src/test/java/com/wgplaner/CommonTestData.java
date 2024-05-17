package com.wgplaner;

import com.wgplaner.core.AuthServer;
import com.wgplaner.core.entity.UserProfile;
import com.wgplaner.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonTestData {
    private final UserRepository userRepository;
    public static UserProfile createUser() {
        return new UserProfile("foo", "abc", "121", 1l, AuthServer.HOME_BREW);
    }

    public UserProfile createSavedUser() {
        return userRepository.save(createUser());
    }
}
