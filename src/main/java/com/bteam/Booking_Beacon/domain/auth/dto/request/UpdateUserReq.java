package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdateUserReq {
    private final Long userId;
    private final String userName;
    private final String password;
    private final String userEmail;
}
