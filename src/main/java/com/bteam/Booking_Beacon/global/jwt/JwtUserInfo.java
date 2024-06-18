package com.bteam.Booking_Beacon.global.jwt;

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
public class JwtUserInfo {
    private Long userId;
    private String username;
}
