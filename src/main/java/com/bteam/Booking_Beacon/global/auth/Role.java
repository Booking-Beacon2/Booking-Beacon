package com.bteam.Booking_Beacon.global.auth;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum Role {
    USER ("USER"),
    PARTNER("USER,PARTNER"),
    ADMIN ("ADMIN,PARTNER,USER"),
    TEST_USER ("TEST_USER,PARTNER,USER"),
    TEST_PARTNER("TEST_USER,TEST_PARTNER,PARTNER,USER"),
    ;

    @JsonValue
    private final String value;
    Role(String value) { this.value = value; }
}
