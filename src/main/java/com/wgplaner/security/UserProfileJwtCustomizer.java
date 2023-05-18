package com.wgplaner.security;

import com.wgplaner.core.AuthServer;
import com.wgplaner.core.repository.UserRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserProfileJwtCustomizer implements Converter<Jwt, MyAuthenticationToken> {
    private final UserRepository userRepository;

    public UserProfileJwtCustomizer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public MyAuthenticationToken convert(Jwt jwt) {
        var principal = userRepository.findByOidAndAuthServer(Long.parseLong(jwt.getClaimAsString("oid")), AuthServer.HOME_BREW);
        return new MyAuthenticationToken(jwt, principal);
    }
}
