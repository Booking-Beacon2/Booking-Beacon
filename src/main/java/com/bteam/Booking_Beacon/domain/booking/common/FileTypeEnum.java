package com.bteam.Booking_Beacon.domain.booking.common;

import lombok.Getter;

@Getter
public enum FileTypeEnum {
    MUSICAL("musical"),
    CONCERT("concert");

    private final String value;
    FileTypeEnum(String value) {
        this.value = value;
    }
}
