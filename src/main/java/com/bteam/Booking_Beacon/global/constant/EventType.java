package com.bteam.Booking_Beacon.global.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum EventType {
    CONCERT("concert"),
    MUSICAL("musical"),

    ;

    @JsonValue
    private final String value;

    /**
     *
     * @param value enum valid 에 들어가는 input 값
     * @return 일치하는 것이 있으면 enum value, 일치 하지 않으면 null
     * @description json payload 에서 string 을 enum 화 시켜서 받을 수 있다.
     */
    @JsonCreator
    public static EventType forValue(String value) {
        return Arrays.stream(values()).filter(v -> v.value.equals(value)).findFirst().orElse(null);
    }

    EventType(String value) {
        this.value = value;
    }

}

