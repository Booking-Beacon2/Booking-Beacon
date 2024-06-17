package com.bteam.Booking_Beacon.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 개발자가 작성한 try catch 에러를 핸들링하기 위함
 */
@Getter
@RequiredArgsConstructor
public class UnHandledUserException extends RuntimeException {
    private final String message;
}
