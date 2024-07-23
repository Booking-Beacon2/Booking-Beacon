package com.bteam.Booking_Beacon.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TokenRes {
    private String accessToken;
    private String refreshToken;
}
