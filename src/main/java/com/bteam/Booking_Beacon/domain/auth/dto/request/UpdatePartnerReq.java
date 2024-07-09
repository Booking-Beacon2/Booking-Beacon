package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Builder
@Getter
public class UpdatePartnerReq {
    private final Optional<String> partnerName;
    private final Optional<String> ein;
    private final Optional<String> phoneNumber;
}
