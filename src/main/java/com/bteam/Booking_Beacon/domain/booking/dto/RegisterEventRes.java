package com.bteam.Booking_Beacon.domain.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class RegisterEventRes {
    private Long musicalId;
    private Long concertId;
}
