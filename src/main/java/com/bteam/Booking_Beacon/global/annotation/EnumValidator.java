package com.bteam.Booking_Beacon.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
    private ValidEnum annotation;


    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum anEnum, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;
        Object[] enumConstants = this.annotation.enumClass().getEnumConstants();
        if (enumConstants != null) {
            for (Object enumConstant : enumConstants) {
                if (enumConstant.toString().equals(anEnum.toString())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
