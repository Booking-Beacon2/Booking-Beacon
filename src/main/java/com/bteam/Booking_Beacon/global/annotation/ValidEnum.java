package com.bteam.Booking_Beacon.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "Invalid Enum"; // 오류 발생시 생성되는 메세지
    Class<?>[] groups() default {}; // 상황별 validation 을 제어하기 위함
    Class<? extends Payload>[] payload() default {}; // 심각도
    Class<? extends java.lang.Enum<?>> enumClass(); // 해당 annotation 이 붙을 수 있는 범위 -> enum 에만 붙을 수 있다.
}
