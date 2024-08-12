package com.bteam.Booking_Beacon.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Builder
@Getter
public class UpdatePartnerReq {
    private final String partnerName;

    @Length(min = 10, max = 10, message = "사업자 등록번호는 숫자로만 구성된 10자리 문자열이어야 합니다.")
    private final String ein;

    @Length(max = 11, message = "전화번호는 최대 11자리까지 입력 가능합니다.")
    private final String phoneNumber;
}
