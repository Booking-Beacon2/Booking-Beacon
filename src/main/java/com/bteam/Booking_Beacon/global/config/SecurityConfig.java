package com.bteam.Booking_Beacon.global.config;

import com.bteam.Booking_Beacon.global.filter.JwtAuthFilter;
import com.bteam.Booking_Beacon.global.jwt.CustomUserDetailsService;
import com.bteam.Booking_Beacon.global.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Repository;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrfConfig) -> csrfConfig.disable())
            .headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));

        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/auth/join").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/info").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/swagger/**").permitAll()
                        .anyRequest().authenticated()
        );



        return http.build();
    }
}
