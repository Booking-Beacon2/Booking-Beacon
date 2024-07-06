package com.bteam.Booking_Beacon.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;

/**
 * ValidEnum: 어떤 annotation interface 를 사용할건지
 * Enum: 해당 annotation 이 어떤 타입의 validation 인지
 */
@Slf4j
public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {
    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    /**
     * @return true : 정상 / false : MethodArgumentNotValidException
     */
    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;

        Object[] enumConstants = this.annotation.enumClass().getEnumConstants();
        if (enumConstants != null) {
            for (Object enumConstant : enumConstants) {
                if (enumConstant.toString().equals(value.toString())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
