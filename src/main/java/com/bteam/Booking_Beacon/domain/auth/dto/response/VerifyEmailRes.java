package com.bteam.Booking_Beacon.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class VerifyEmailRes {
    private String authCode;
}
