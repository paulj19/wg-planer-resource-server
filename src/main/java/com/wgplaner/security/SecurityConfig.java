package com.wgplaner.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       // http
       //         .authorizeHttpRequests()
       //             .antMatchers("/register").permitAll()
       //             .antMatchers("/prometheus").permitAll()
       //         .anyRequest().authenticated()
       //         .and()
       //             .formLogin()
       //             .loginPage(SecurityUrls.LOGIN_URL)
       //             .loginProcessingUrl(SecurityUrls.LOGIN_PROCESSING_URL)
       //             .defaultSuccessUrl(SecurityUrls.LOGIN_SUCCESS_URL)
       //         .and()
       //             .logout()
       //             .logoutUrl(SecurityUrls.LOGOUT_URL)
       //             .logoutSuccessUrl(SecurityUrls.LOGIN_URL);
        return http.cors().and().csrf().disable().build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedMethods("*").allowedHeaders("*");
            }
        };
    }
}
