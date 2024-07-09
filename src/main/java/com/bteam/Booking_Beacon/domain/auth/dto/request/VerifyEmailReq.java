package com.bteam.Booking_Beacon.domain.auth.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
//@NoArgsConstructor
public class VerifyEmailReq {
    @NotNull
    private final String userEmail;

    /*
    생성자
     */
    @JsonCreator
    VerifyEmailReq(String userEmail) {
        this.userEmail = userEmail;
    }
}