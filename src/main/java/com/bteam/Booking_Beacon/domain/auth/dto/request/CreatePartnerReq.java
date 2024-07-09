package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePartnerReq {
    private final String partnerName;
    private final String ein;
    private final String phoneNumber;
}
