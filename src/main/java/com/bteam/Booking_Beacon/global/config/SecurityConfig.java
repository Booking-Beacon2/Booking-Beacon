package com.bteam.Booking_Beacon.global.config;

import com.bteam.Booking_Beacon.global.auth.Role;
import com.bteam.Booking_Beacon.global.filter.JwtAuthFilter;
import com.bteam.Booking_Beacon.global.auth.CustomUserDetailsService;
import com.bteam.Booking_Beacon.global.jwt.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.PrintWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf((csrfConfig) -> csrfConfig.disable())
            .headers((headerConfig) -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()));

        http.addFilterBefore(new JwtAuthFilter(customUserDetailsService, jwtService), UsernamePasswordAuthenticationFilter.class);

        // http 요청에 대한 인가 설정
        http.authorizeHttpRequests(
                authorize -> authorize
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .requestMatchers("/swagger/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/beacon/sse-subscribe").permitAll()
                        .requestMatchers("/auth/info", "/auth/login", "/auth/join", "/auth/join-partner", "/auth/verify/email", "/auth/refresh-token").permitAll()
                        .requestMatchers("/auth/user", "/auth/users").hasAnyRole(Role.USER.toString(), Role.TEST_USER.toString(), Role.ADMIN.toString())
                        .requestMatchers("/auth/partner", "/auth/partners").hasAnyRole(Role.PARTNER.toString(), Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, "/event").hasAnyRole(Role.PARTNER.toString(), Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET, "/event").hasAnyRole(Role.USER.toString(),Role.PARTNER.toString(), Role.ADMIN.toString())
                        .anyRequest().authenticated()
        );

        http.exceptionHandling((exceptionConfig) ->
                exceptionConfig.authenticationEntryPoint(unauthorizedEntryPoint).accessDeniedHandler(accessDeniedHandler)
        ); // 401 403 관련 예외처리

        return http.build();
    }

    // 401
    private final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ErrorResponse fail = ErrorResponse.builder().code("UNAUTHORIZED").message("Unauthorized").build();
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    // 403
    private final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                ErrorResponse fail = ErrorResponse.builder().code("ACCESS_DENIED").message("Access denied").build();
                response.setStatus(HttpStatus.FORBIDDEN.value());
                String json = new ObjectMapper().writeValueAsString(fail);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                PrintWriter writer = response.getWriter();
                writer.write(json);
                writer.flush();
            };

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class ErrorResponse {

        private final String message;
        private final String code;
        private final String stackTrace;
    }
}
