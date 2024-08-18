package com.bteam.Booking_Beacon.global.constant;

import lombok.Getter;

@Getter
public enum BookingStatusType {
    PAID("RESERVED"), // 예약완료
    CANCEL_BY_USER("CANCEL_BY_USER"), // 취소완료_고객
    CANCEL_BY_PARTNER("CANCEL_BY_PARTNER"), // 취소완료_파트너
    CANCEL_BY_ADMIN("CANCEL_BY_ADMIN") // 취소완료_관리자
    ;

    private final String value;
    BookingStatusType(String value) {
        this.value = value;
    }
}
