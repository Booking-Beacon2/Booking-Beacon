package com.bteam.Booking_Beacon.global.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class JwtUserInfo {
    private Long userId;
    private String username;
}
