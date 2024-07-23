package com.bteam.Booking_Beacon.domain.auth.dto.request;

import com.bteam.Booking_Beacon.global.annotation.ValidEnum;
import com.bteam.Booking_Beacon.global.constant.EventType;
import com.bteam.Booking_Beacon.global.constant.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
public class VerifyEmailReq {
    @NotNull
    private final String email;

    @NotNull
    @ValidEnum(enumClass = UserType.class)
    private final UserType userType;
}