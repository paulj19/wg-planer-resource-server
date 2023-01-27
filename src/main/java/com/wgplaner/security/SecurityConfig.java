package com.wgplaner.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Import(OAuth2AuthorizationServerConfiguration.class)
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

//        @Bean
//        SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//            return http
//                    .authorizeRequests(authorizeRequests ->
//                            authorizeRequests.anyRequest().authenticated())
//                    .build();
//        }
//
//        @Bean
//        @Order(Ordered.HIGHEST_PRECEDENCE)
//        public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
//            OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
//            return http.formLogin(Customizer.withDefaults()).build();
//        }
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
//                    new OAuth2AuthorizationServerConfigurer();
//            RequestMatcher endpointsMatcher = authorizationServerConfigurer
//                    .getEndpointsMatcher();
            http
//                    .requestMatcher(endpointsMatcher)
                    .authorizeRequests(authorizeRequests ->
                            authorizeRequests.anyRequest().authenticated());
//                    .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
//                    .apply(authorizationServerConfigurer);
//
            return http.build();
        }

        @Bean
        RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
            RegisteredClient registeredClient = RegisteredClient
                    .withId(UUID.randomUUID().toString())
                    .clientId("wg-planer")
                    .clientSecret(passwordEncoder.encode("secret"))
                    .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .build();
            return new InMemoryRegisteredClientRepository(registeredClient);
        }

        @Bean
        public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
            RSAKey rsaKey = generateRsa();
            JWKSet jwkSet = new JWKSet(rsaKey);
            return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
        }

        private static RSAKey generateRsa() throws NoSuchAlgorithmException {
            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
        }

        private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        }

//        @Bean
//        public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
//            return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
//        }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    @Bean
    //    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    //        return authenticationConfiguration.getAuthenticationManager();
    //    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests()
//                .antMatchers("/registration/**").permitAll()
//                .antMatchers("/prometheus/**").permitAll();
//                .and()
//                .httpBasic()
//                .and()
//                .userDetailsService(new InMemoryUserDetailsManager(new User("username", "password", "email@abc.com")))
//                .authorizeHttpRequests().anyRequest().authenticated();
    //         .and()
    //             .formLogin()
    //             .loginPage(SecurityUrls.LOGIN_URL)
    //             .loginProcessingUrl(SecurityUrls.LOGIN_PROCESSING_URL)
    //             .defaultSuccessUrl(SecurityUrls.LOGIN_SUCCESS_URL)
    //         .and()
    //             .logout()
    //             .logoutUrl(SecurityUrls.LOGOUT_URL)
    //             .logoutSuccessUrl(SecurityUrls.LOGIN_URL);
//        return http.cors().and().csrf().disable().build();
//    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedMethods("*").allowedHeaders("*");
//            }
//        };
//    }

}
