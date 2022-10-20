package com.wgplaner;

import com.wgplaner.core.entity.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT {
    @Autowired
    private CommonTestData commonTestData;

    protected User getTestUser() {
        return commonTestData.createSavedUser();
    }
}
