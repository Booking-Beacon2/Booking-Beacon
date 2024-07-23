package com.bteam.Booking_Beacon.global.constant;

import lombok.Getter;

@Getter
public enum UserType {
    USER("USER"),
    PARTNER("PARTNER"),
    ;

    private final String value;
    UserType(String value) {
        this.value = value;
    }
}
