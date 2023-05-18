package com.wgplaner.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "authserver.home-brew")
public class AuthServerConfig {
    private String clientId;
    private String clientSecret;
    private String uri;
}
