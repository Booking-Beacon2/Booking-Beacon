package com.bteam.Booking_Beacon.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshAccessTokenReq {
    @NotNull
    private final String refreshToken;

    /*
    생성자
     */
    @JsonCreator
    RefreshAccessTokenReq(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}