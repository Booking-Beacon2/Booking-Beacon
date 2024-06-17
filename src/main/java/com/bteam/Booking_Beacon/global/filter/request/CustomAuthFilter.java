//package com.bteam.Booking_Beacon.global.filter.request;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.security.Key;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * api 요청 시 token 이 담겨 있는지 여부 확인 -> 여기서 토큰의 유효성 검사가 필요
// */
//@Component
//@Slf4j
//public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config> {
//
//    private final Key key;
//
//    public CustomAuthFilter(@Value("${jwt.secret}") String secretKey, @Value("${jwt.access-token-validity-in-seconds}") long tokenValidityInSeconds) {
//        super(Config.class);
//        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
//        this.key = Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    /**
//     *
//     * @param httpMethod
//     * @param url
//     * @return gatewayFilter 를 적용하는 경우 true, 적용 안하면 false
//     */
//    private boolean isFilterApplied(String httpMethod, String url) {
//        Map<String, String> excludedUrl = new HashMap<>();
//        excludedUrl.put("/auth/login", "POST"); // 로그인
//        excludedUrl.put("/auth/user", "POST"); // 회원가입
//        if (excludedUrl.containsKey(url)) {
//            return !excludedUrl.get(url).equals(httpMethod);
//        } else {
//            return true;
//        }
//    }
//
//
//    @Override
//    public GatewayFilter apply(Config config) {
//        return new OrderedGatewayFilter((exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            /*
//             * excludedUrl 을 제외한 부분에서만 토큰 검사
//             */
//            String httpMethod = request.getMethod().toString();
//            String url = exchange.getRequest().getURI().getPath();
//            /**
//             * Response-Type : 원하는 response format
//             * client 에서 Response-Format 을 null 로 보내면 application/json 으로 요청 보냄.
//             * 원하는 포맷이 있다면 다른 Response-Format 으로 보냄.
//             * ->  추후에 response-format 에 영향을 줌 (ApiResponseFilter 참조)
//             */
//
//            if (this.isFilterApplied(httpMethod, url)) {
//                if (!request.getHeaders().containsKey("Authorization")) {
//                    return handleUnauthorized(exchange);
//                }
//
//                List<String> token = (request.getHeaders().get("Authorization"));
//
//                assert token != null;
//
//                if (token.isEmpty() || !token.get(0).startsWith("Bearer ")) {
//                    return handleUnauthorized(exchange);
//                }
//                JwtUserInfo jwtUserInfo = this.getUserFromToken(token.get(0).split(" ")[1]);
//                if (jwtUserInfo.getUserId() == null) {
//                    return handleUnauthorized(exchange);
//                }
//                exchange.getRequest().mutate().header("userId", jwtUserInfo.getUserId().toString()).build();
//            }
//
//            return chain.filter(exchange);
//        }, 0);
//    }
//
//    private Mono<Void> handleUnauthorized(ServerWebExchange exchange) {
//        ServerHttpResponse response = exchange.getResponse();
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//
//        return response.setComplete();
//    }
//
//    public static class Config {
//
//    }
//
//    /**
//     * 토큰에서 claims 추출
//     */
//    public Claims parseToken(String token) {
//        try {
//            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
//        } catch (ExpiredJwtException e) {
//            return e.getClaims();
//        }
//    }
//
//    /**
//     * 토큰에서 유저 정보 추출
//     */
//    public JwtUserInfo getUserFromToken(String token) {
//        Claims claims = parseToken(token);
//        long userId = claims.get("userId", Long.class);
//        String username = claims.get("username", String.class);
//        return JwtUserInfo.builder().userId(userId).username(username).build();
//    }
//}