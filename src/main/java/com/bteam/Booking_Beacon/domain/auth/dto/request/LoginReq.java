package com.bteam.Booking_Beacon.domain.auth.dto.request;

import com.bteam.Booking_Beacon.global.constant.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginReq {
    @NotNull
    private final String userEmail;

    @NotNull
    private final String password;

    @NotNull
    private final UserType userType;
}
