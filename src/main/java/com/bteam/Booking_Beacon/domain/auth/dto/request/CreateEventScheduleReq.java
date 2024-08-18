package com.bteam.Booking_Beacon.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class CreateEventScheduleReq {
    @NotNull
    private Date startDate;

    @NotNull
    private Long runningTime;

    @NotNull
    private Integer capacity;
}
