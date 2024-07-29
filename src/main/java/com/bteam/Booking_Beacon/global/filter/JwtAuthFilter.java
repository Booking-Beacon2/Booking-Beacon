package com.bteam.Booking_Beacon.global.filter;

import com.bteam.Booking_Beacon.global.constant.UserType;
import com.bteam.Booking_Beacon.global.auth.CustomUserDetailsService;
import com.bteam.Booking_Beacon.global.jwt.JwtPayload;
import com.bteam.Booking_Beacon.global.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class JwtAuthFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 한 번 실행 보장

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);

        Long userId = null;
        Long partnerId = null;
        UserType userType = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            String token = authorizationHeader.substring(TOKEN_PREFIX.length());
            //JWT 유효성 검증
            if (jwtService.validateToken(token)) {
                JwtPayload jwtPayload = jwtService.getJwtPayloadFromToken(token);
                userType = jwtPayload.getUserType();
                UserDetails userDetails = null;

                switch (userType) {
                    case USER -> {
                        userId = jwtPayload.getUserId();
                        userDetails = customUserDetailsService.loadUserById(UserType.USER, jwtPayload.getUserId());
                    }
                    case PARTNER -> {
                        partnerId = jwtPayload.getPartnerId();
                        userDetails = customUserDetailsService.loadUserById(UserType.PARTNER, jwtPayload.getPartnerId());
                    }
                }

                if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    //UserDetsils, Password, Role -> 접근권한 인증 Token 생성
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    //현재 Request의 Security Context에 접근권한 설정
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        request.setAttribute("userId", userId);
        request.setAttribute("partnerId", partnerId);
        // 여기까지 request filter
        filterChain.doFilter(request, response); // request, response 기준점 (request, response 를 담아서 다음 필터로 이동)
        // 여기부터 response filter
    }
}
