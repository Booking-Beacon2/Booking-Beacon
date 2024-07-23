package com.bteam.Booking_Beacon.domain.auth.dto.request;

import com.bteam.Booking_Beacon.global.annotation.ValidEnum;
import com.bteam.Booking_Beacon.global.constant.UserType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Builder
@Getter
public class VerifyEmailAuthCodeReq {
    @NotNull
    @Length(min = 6, max = 6)
    private String authCode;

    @NotNull
    private final String email;

    @NotNull
    @ValidEnum(enumClass = UserType.class)
    private final UserType userType;
}
