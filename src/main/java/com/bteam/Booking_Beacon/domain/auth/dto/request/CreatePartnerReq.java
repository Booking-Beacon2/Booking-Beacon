package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
public class CreatePartnerReq {
    private final String partnerName;

    @Length(min = 10, max = 10)
    private final String ein;

    private final String phoneNumber;
}
