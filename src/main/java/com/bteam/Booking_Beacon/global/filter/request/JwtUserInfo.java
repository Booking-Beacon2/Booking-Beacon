package com.bteam.Booking_Beacon.global.filter.request;

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