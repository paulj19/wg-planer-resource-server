package com.wgplaner;

import com.wgplaner.core.entity.User;

public class CommonTestData {
    public static User createUser() {

        return new User("foo", "abc", "diljosepaul@gmail.com");
    }
}
