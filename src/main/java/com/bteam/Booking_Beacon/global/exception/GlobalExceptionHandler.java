package com.bteam.Booking_Beacon.global.exception;

import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.InvalidParameterException;
import java.util.Arrays;

/**
 * 발생하는 모든 에러를 여기서 처리함.
 * ResponseEntityExceptionHandler : Spring MVC Exception 에러(Http 요청/응답)를 모두 처리함
 */
@Slf4j
@ControllerAdvice()
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 1. 개발자가 핸들링 하는 에러 (로직 상에서 발생하는 에러)
    @ExceptionHandler(RestApiException.class)
    protected final ResponseEntity<Object> handleBBCustomException(RestApiException ex) {
        log.info("RestApiException");
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    // 2. try catch 에서 잡히는 unhandled error
    @ExceptionHandler(UnHandledUserException.class)
    protected final ResponseEntity<Object> handleBBUnhandledUserException(UnHandledUserException ex) {
        log.info("UnHandledUserException");
        ErrorCode errorCode = CommonErrorCode.UNHANDLED_EXCEPTION;
        String stackTrace = String.valueOf(Arrays.stream(ex.getStackTrace()).findFirst());
        return handleExceptionInternal(errorCode, ex.getMessage(), stackTrace);
    }

    /*
     여기까지 코드로 작성할 수 있는 에러 쓰로잉
     아래부터는 개발자가 잡을 수 없는 에러
     */

    // 일반 에러 + 일치하는 에러가 없을 경우 + ResponseEntityExceptionHandler 속하지 않는 경우 여기로 온다
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.info("Exception");
        log.info(Arrays.toString(new Class[]{ex.getClass()}));
        ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
        String stackTrace = String.valueOf(Arrays.stream(ex.getStackTrace()).findFirst());
        return handleExceptionInternal(errorCode, ex.getMessage(), stackTrace);
    }

    // @ValidEnum 으로 걸리는 경우
    @ExceptionHandler(ValidationException.class)
    protected final ResponseEntity<Object> handleValidationException(ValidationException ex) {
        log.info("ValidationException");
        log.info(ex.getMessage());
        ErrorCode errorCode = CommonErrorCode.VALIDATION_ERROR;
        String stackTrace = String.valueOf(Arrays.stream(ex.getStackTrace()).findFirst());
        return handleExceptionInternal(errorCode, ex.getMessage(), stackTrace);
    }


    // null pointer error
    @ExceptionHandler(NullPointerException.class)
    protected final ResponseEntity<Object> handleNullPointerException(NullPointerException ex) {
        log.info("NullPointerException");
        ErrorCode errorCode = CommonErrorCode.NULL_POINTER;
        String stackTrace = String.valueOf(Arrays.stream(ex.getStackTrace()).findFirst());
        return handleExceptionInternal(errorCode, ex.getMessage(), stackTrace);
    }



    @ExceptionHandler(UnexpectedTypeException.class)
    protected final ResponseEntity<Object> handleUnexpectedTypeException(UnexpectedTypeException ex) {
        log.info("UnexpectedTypeException");
        ErrorCode errorCode = CommonErrorCode.UNEXPECTED_TYPE_ERROR;
        String stackTrace = String.valueOf(Arrays.stream(ex.getStackTrace()).findFirst());
        return handleExceptionInternal(errorCode, ex.getMessage(), stackTrace);
    }

    // 잘못된 http method
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("handleHttpRequestMethodNotSupported");
        ErrorCode errorCode = CommonErrorCode.INVALID_HTTP_METHOD;
        String stackTrace = String.valueOf(Arrays.stream(ex.getStackTrace()).findFirst());
        return handleExceptionInternal(errorCode, ex.getMessage(), stackTrace);
    }

    // dto valid error
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("handleMethodArgumentNotValid");
        ErrorCode errorCode = CommonErrorCode.METHOD_ARG_NOT_VALID;
        String stackTrace = String.valueOf(Arrays.stream(ex.getStackTrace()).findFirst());
        return handleExceptionInternal(errorCode, ex.getMessage(), stackTrace);
    }



    /*

     */

    /**
     * @param errorCode 개발자가 직접 발생한 런타임 에러코드
     */
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.httpStatus()).body(makeErrorResponse(errorCode));
    }

    private CustomErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return CustomErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    /**
     * @param errorCode    java 내에서 발생하는 런타임 에러코드
     * @param errorMessage
     */
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String errorMessage, String stackTrace) {
        return ResponseEntity.status(errorCode.httpStatus()).body(makeErrorResponse(errorCode, errorMessage, stackTrace));
    }

    private CustomErrorResponse makeErrorResponse(ErrorCode errorCode, String errorMessage, String stackTrace) {
        return CustomErrorResponse.builder()
                .code(errorCode.name())
                .stackTrace(stackTrace)
                .message(errorMessage != null ? errorMessage : errorCode.getMessage())
                .build();
    }
}
