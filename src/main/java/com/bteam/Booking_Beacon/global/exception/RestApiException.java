package com.bteam.Booking_Beacon.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 개발자가 작성한 로직상 에러를 처리하기 위함.
 */
@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode;

}
