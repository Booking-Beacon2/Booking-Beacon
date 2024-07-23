package com.bteam.Booking_Beacon.global.jwt;

import com.bteam.Booking_Beacon.global.constant.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class JwtUtil {
    private final Key key;
    private final long accessTokenValidityInSeconds;
    private final long refreshTokenValidityInSeconds;

    public JwtUtil(@Value("${jwt.secret}")String secretKey, @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds, @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityInSeconds = accessTokenValidityInSeconds;
        this.refreshTokenValidityInSeconds = refreshTokenValidityInSeconds;
    }

    /** 토큰 생성 */
    public String createToken(JwtPayload jwtPayload, String tokenType) {
        Claims claims = Jwts.claims();
        claims.put("userType", jwtPayload.getUserType());
        claims.put("userId", jwtPayload.getUserId());
        claims.put("partnerId", jwtPayload.getPartnerId());
        claims.put("email", jwtPayload.getEmail());

        long now = (new Date()).getTime();
        long validityInSeconds = Objects.equals(tokenType, "access") ? accessTokenValidityInSeconds : Objects.equals(tokenType, "refresh") ? refreshTokenValidityInSeconds : 0;
        Date expiration = new Date(now + validityInSeconds * 1000);

        return Jwts.builder().setClaims(claims).setExpiration(expiration).signWith(SignatureAlgorithm.HS256, key).compact();
    }

    /** 토큰에서 claims 추출 */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    /** 토큰에서 유저 정보 추출 */
    public JwtPayload getJwtPayloadFromToken(String token) {
        Claims claims = parseToken(token);
        UserType userType = UserType.valueOf(claims.get("userType", String.class));
        String email = claims.get("email", String.class);
        long exp = claims.get("exp", Long.class);

        JwtPayload jwtPayload = null;
        switch (userType) {
            case USER -> {
                long userId = claims.get("userId", Long.class);
                jwtPayload = JwtPayload.builder().userType(userType).userId(userId).email(email).exp(exp).build();
            }
            case PARTNER -> {
                long partnerId = claims.get("partnerId", Long.class);
                jwtPayload = JwtPayload.builder().userType(userType).partnerId(partnerId).email(email).exp(exp).build();
            }
        }

        return jwtPayload;
    }

    /** 토큰이 정상적인 형태인지 여부 */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }
}
