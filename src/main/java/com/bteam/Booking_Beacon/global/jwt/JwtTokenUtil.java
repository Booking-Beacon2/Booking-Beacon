package com.bteam.Booking_Beacon.global.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtil {
    private final Key key;
    private final long tokenValidityInSeconds;

    public JwtTokenUtil(@Value("${jwt.secret}")String secretKey, @Value("${jwt.access-token-validity-in-seconds}") long tokenValidityInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }

    /**
     * 토큰 생성
     */
    public String createToken(JwtUserInfo jwtUserInfo) {
        Claims claims = Jwts.claims();
        claims.put("userId", jwtUserInfo.getUserId());
        claims.put("username", jwtUserInfo.getUsername());

        long now = (new Date()).getTime();
        Date expiration = new Date(now + tokenValidityInSeconds * 1000);

        return Jwts.builder().setClaims(claims).setExpiration(expiration).signWith(SignatureAlgorithm.HS256, key).compact();
    }

    /**
     * 토큰에서 claims 추출
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /**
     * 토큰에서 유저 정보 추출
     */
    public JwtUserInfo getUserFromToken(String token) {
        Claims claims = parseToken(token);
        long userId = claims.get("userId", Long.class);
        String username = claims.get("username", String.class);
        return JwtUserInfo.builder().userId(userId).username(username).build();
    }
}
