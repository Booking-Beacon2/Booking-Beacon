package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class VerifyEmailReq {
    private String userEmail;
//    private String password;
}