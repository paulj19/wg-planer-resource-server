package com.wgplaner.security;

import com.wgplaner.common.url.url_store.SecurityUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements SecurityFilterChain {
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) -> {
            try {
                authz.anyRequest().authenticated().and().formLogin().loginPage(SecurityUrls.LOGIN_URL).loginProcessingUrl(SecurityUrls.LOGIN_PROCESSING_URL).defaultSuccessUrl(SecurityUrls.LOGIN_SUCCESS_URL).and().logout().logoutUrl(SecurityUrls.LOGOUT_URL).logoutSuccessUrl(SecurityUrls.LOGIN_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).httpBasic(withDefaults());
        return http.build();
    }

    //@Bean
    // public WebMvcConfigurer corsConfigurer() {
    //    return new WebMvcConfigurer() {
    //      @Override
    //        public void addCorsMappings(CorsRegistry registry) {
    //            registry.addMapping("/**")
    //                    .allowedMethods("*");
    //        }
    //    };
    //}

    @Override
    public boolean matches(HttpServletRequest request) {
        return false;
    }

    @Override
    public List<Filter> getFilters() {
        return null;
    }
}
