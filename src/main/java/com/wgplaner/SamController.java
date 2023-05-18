package com.wgplaner;

import com.wgplaner.core.entity.UserProfile;
import com.wgplaner.registration.UserProfileDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sam")
public class SamController {
    @GetMapping("/user-details")
    public UserProfileDto getUserDetails(@AuthenticationPrincipal UserProfile userProfile){
        return new UserProfileDto(userProfile.getId(), userProfile.getUsername(), userProfile.getEmail(), userProfile.getOid(), userProfile.getAuthServer());
    }
}
