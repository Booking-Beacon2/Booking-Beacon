package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Builder
@Getter
public class UpdatePartnerReq {
    private Optional<String> partnerName;
    private Optional<String> ein;
    private Optional<String> phoneNumber;
}
