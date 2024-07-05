package com.bteam.Booking_Beacon.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    /**
     * BB prefix error : rest api exception
     */
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation Error"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    UNEXPECTED_TYPE_ERROR(HttpStatus.BAD_REQUEST, "Unexpected type error"),
    METHOD_ARG_NOT_VALID(HttpStatus.BAD_REQUEST, "method argument not valid"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    INVALID_HTTP_METHOD(HttpStatus.METHOD_NOT_ALLOWED, "Invalid HTTP method"),
    BB_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 유저입니다"),
    INVALID_URL(HttpStatus.BAD_REQUEST, "Invalid URL"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    UNHANDLED_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unhandled exception"),
    NULL_POINTER(HttpStatus.INTERNAL_SERVER_ERROR, "Null pointer exception"),
    BB_PASSWORD_INCORRECT(HttpStatus.BAD_REQUEST, "Password incorrect"),
    BB_EMAIL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다"),
    BB_PARTNER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "동일 사업자번호로 등록된 파트너가 이미 존재합니다."),
    BB_PARTNER_NOT_FOUND(HttpStatus.NOT_FOUND, "파트너 정보가 존재하지 않습니다."),
    BB_FILE_NOT_IMAGE(HttpStatus.FORBIDDEN, "파일이 이미지 형태가 아닙니다."),
    BB_FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "파일 업로드에 실패했습니다."),
    BB_FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "파일이 존재하지 않습니다."),
    BB_FILE_DOWNLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운에 실패했습니다.")
    ;

    // 여기서 상속 받은 getName 이 enum 의 name 즉 USER_NOT_FOUND 를 가져온다.
    // name(httpStatus, message) 이런 구조가 된다.

    private final HttpStatus httpStatus;
    private final String message;


    @Override
    public HttpStatus httpStatus() {
        return this.httpStatus;
    }
}