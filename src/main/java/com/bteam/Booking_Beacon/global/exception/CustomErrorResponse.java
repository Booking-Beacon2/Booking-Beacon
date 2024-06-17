package com.bteam.Booking_Beacon.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CustomErrorResponse {

    private final String code;
    private final String message;
    private final String stackTrace;

}