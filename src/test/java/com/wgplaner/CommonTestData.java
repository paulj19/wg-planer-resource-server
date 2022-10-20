package com.wgplaner;

import com.wgplaner.core.entity.User;
import com.wgplaner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonTestData {
    private final UserRepository userRepository;
    public static User createUser() {
        return new User("foo", "abc", "diljosepaul@gmail.com");
    }

    public User createSavedUser() {
        return userRepository.save(createUser());
    }
}
