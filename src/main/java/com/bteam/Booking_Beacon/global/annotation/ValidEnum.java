package com.bteam.Booking_Beacon.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "Invalid Enum";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    Class<? extends java.lang.Enum> enumClass() default java.lang.Enum.class;
}
