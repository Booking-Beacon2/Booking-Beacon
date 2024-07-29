package com.bteam.Booking_Beacon.global.jwt;

import com.bteam.Booking_Beacon.global.auth.Role;
import com.bteam.Booking_Beacon.global.constant.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * @description 토큰에 담을 정보
 * @ 토큰 DTO
 */
@Getter
@Builder
@AllArgsConstructor
public class JwtPayload {
    private Long userId;
    private Long partnerId;
    private UserType userType;
    private String email;
    private Long exp;
    private Long iat;
    private Role role;
}
