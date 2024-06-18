package com.bteam.Booking_Beacon.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrfConfig) -> csrfConfig.disable())
            .headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/auth/join").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/info").permitAll()
                        .anyRequest().authenticated()
        );
        return http.build();
    }
}
