//package com.bteam.Booking_Beacon.global.filter;
//
//import com.bteam.Booking_Beacon.global.exception.CommonErrorCode;
//import com.bteam.Booking_Beacon.global.exception.ErrorCode;
//import com.bteam.Booking_Beacon.global.exception.UnHandledUserException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.Ordered;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.ErrorResponse;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
///**
// * auth filter (token) 에 의해 발생되는 에러를 관리
// */
//@Component
//@Slf4j
//public class AuthExceptionFilter extends OncePerRequestFilter implements Ordered {
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try{
//            filterChain.doFilter(request, response);
//        }catch (ExpiredJwtException e){
//            //토큰의 유효기간 만료
//            setErrorResponse(response, CommonErrorCode.INVALID_TOKEN);
//        }catch (JwtException | IllegalArgumentException e){
//            //유효하지 않은 토큰
//            setErrorResponse(response, CommonErrorCode.INVALID_TOKEN);
//        }catch (UnHandledUserException e) {
//            setErrorResponse(response, CommonErrorCode.INVALID_TOKEN);
//        }
//    }
//
//    private void setErrorResponse(
//            HttpServletResponse response,
//            CommonErrorCode errorCode
//    ){
//        ObjectMapper objectMapper = new ObjectMapper();
////        response.setStatus(errorCode.getHttpStatus().value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
////        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
//        try{
//            response.getWriter().write("invalid token");
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
