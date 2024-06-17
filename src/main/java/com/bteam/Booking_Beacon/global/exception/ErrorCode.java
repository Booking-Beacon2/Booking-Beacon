package com.bteam.Booking_Beacon.global.exception;


import org.springframework.http.HttpStatus;

public interface ErrorCode {
    String name();
    HttpStatus httpStatus();
    String getMessage();
}
