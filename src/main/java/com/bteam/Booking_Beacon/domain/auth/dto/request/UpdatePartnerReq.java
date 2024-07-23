package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public class UpdatePartnerReq {
    private final String partnerName;
    private final String ein;
    private final String phoneNumber;
}
