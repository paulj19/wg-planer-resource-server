package com.wgplaner.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().authenticated())
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
        return http.build();
    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
