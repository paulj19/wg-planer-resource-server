package com.wgplaner.security;

import com.wgplaner.core.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    UserRepository userRepository;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers("/register/**", "/password-recovery/**")
        .permitAll()
        .and()
        .authorizeRequests()
        .antMatchers("/actuator/**")
        .permitAll()
        .and()
        .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
        .csrf()
        .disable()
        // .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .oauth2ResourceServer()
        .jwt(c -> c.jwtAuthenticationConverter(new UserProfileJwtCustomizer(userRepository))).and()
        .cors()
        .configurationSource(
            request -> {
              CorsConfiguration corsConfiguration = new CorsConfiguration();
              corsConfiguration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:19006", "http://localhost", "http://192.168.1.9:8080"));
              corsConfiguration.setAllowCredentials(true);
              corsConfiguration.setAllowedMethods(
                  Arrays.asList(
                      HttpMethod.GET.name(),
                      HttpMethod.HEAD.name(),
                      HttpMethod.POST.name(),
                      HttpMethod.OPTIONS.name()));
              corsConfiguration.setAllowedHeaders(
                  Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
              corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
              corsConfiguration.setMaxAge(1800L);
              // corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
              return corsConfiguration;
            });
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
