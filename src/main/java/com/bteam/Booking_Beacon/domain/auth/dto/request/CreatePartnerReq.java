package com.bteam.Booking_Beacon.domain.auth.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@Setter
public class CreatePartnerReq {
    private final String email;
    private String password;
    private String userName;
    private String partnerName;

    @Length(min = 10, max = 10)
    private final String ein;

    private String phoneNumber;
}
