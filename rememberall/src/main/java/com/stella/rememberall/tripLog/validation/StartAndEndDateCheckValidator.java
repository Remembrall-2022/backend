package com.stella.rememberall.tripLog.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ServerErrorException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

@Slf4j
public class StartAndEndDateCheckValidator implements ConstraintValidator<StartAndEndDateCheck, Object> {
    private String message;
    private String startDate;
    private String endDate;

    @Override
    public void initialize(StartAndEndDateCheck constraintAnnotation) {
        message = constraintAnnotation.message();  // 애노테이션에 저장된 메세지
        startDate = constraintAnnotation.startDate(); // 애노테이션에 저장된 비교할 필드
        endDate = constraintAnnotation.endDate(); // 애노테이션에 저장된 비교할 필드
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        int invalidCount = 0;
        LocalDate tripStartDate = getFieldValue(o, startDate);
        LocalDate tripEndDate = getFieldValue(o, endDate);
        if (tripStartDate.isAfter(tripEndDate)) { // 검증 후 오류가 있다면
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message) // context에 오류메세지와
                    .addPropertyNode(startDate) // 대상 필드를 넣어줍니다.
                    .addConstraintViolation();
            invalidCount += 1;
        }
        return invalidCount == 0;
    }

    // 리플렉션을 이용하여 필드를 가져옴
    private LocalDate getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        Field dateField;
        try {
            dateField = clazz.getDeclaredField(fieldName);
            dateField.setAccessible(true);
            Object target = dateField.get(object);
            if (!(target instanceof LocalDate)) {
                throw new ClassCastException("casting exception");
            }
            return (LocalDate) target;
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
        }
        throw new ServerErrorException("Not Found Field");
    }
}