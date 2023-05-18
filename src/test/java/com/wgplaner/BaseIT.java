package com.wgplaner;

import com.wgplaner.core.entity.UserProfile;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT {
    @Autowired
    private CommonTestData commonTestData;

    protected UserProfile getTestUser() {
        return commonTestData.createSavedUser();
    }
}
