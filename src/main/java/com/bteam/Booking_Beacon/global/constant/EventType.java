package com.bteam.Booking_Beacon.global.constant;

public enum EventType {
    CONCERT("concert"),
    MUSICAL("musical"),

    ;

    private final String value;
    EventType(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
