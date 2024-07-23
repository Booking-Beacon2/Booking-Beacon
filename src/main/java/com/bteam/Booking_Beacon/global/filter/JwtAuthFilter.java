package com.bteam.Booking_Beacon.global.filter;

import com.bteam.Booking_Beacon.global.constant.UserType;
import com.bteam.Booking_Beacon.global.jwt.CustomUserDetailsService;
import com.bteam.Booking_Beacon.global.jwt.JwtPayload;
import com.bteam.Booking_Beacon.global.jwt.JwtUtil;
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

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    /** JWT 토큰 검증 필터 수행 */
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        Long userId = null;
        Long partnerId = null;
        UserType userType = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            //JWT 유효성 검증
            if (jwtUtil.validateToken(token)) {
                JwtPayload jwtPayload = jwtUtil.getJwtPayloadFromToken(token);
                userType = jwtPayload.getUserType();
                UserDetails userDetails = null;

                switch (userType) {
                    case USER -> {
                        userId = jwtPayload.getUserId();
                        userDetails = customUserDetailsService.loadJwtPayloadById(UserType.USER, jwtPayload.getUserId());
                    }
                    case PARTNER -> {
                        partnerId = jwtPayload.getPartnerId();
                        userDetails = customUserDetailsService.loadJwtPayloadById(UserType.PARTNER, jwtPayload.getPartnerId());
                    }
                }

                if (userDetails != null) {
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
        filterChain.doFilter(request, response); // request, response 기준점
        // 여기부터 response filter
    }
}
