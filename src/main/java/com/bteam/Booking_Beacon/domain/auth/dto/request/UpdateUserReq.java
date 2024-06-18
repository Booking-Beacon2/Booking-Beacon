package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UpdateUserReq {
    private Long userId;
    private String userName;
    private String password;
    private String userEmail;
}
