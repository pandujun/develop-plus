package io.github.pandujun.develop.plus.core.validation.validator;

import io.github.pandujun.develop.plus.core.validation.annotation.EnumValid;
import io.github.pandujun.develop.plus.core.validation.annotation.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class EnumValidator implements ConstraintValidator<EnumValid, Object> {

    private ValidEnum[] validEnums;
    private boolean canNull;
    private boolean canBlank;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        Class<? extends ValidEnum> value = constraintAnnotation.value();
        this.validEnums = value.getEnumConstants();
        this.canNull = constraintAnnotation.canNull();
        this.canBlank = constraintAnnotation.canBlank();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            // 空对象
            return canNull || canBlank;
        }
        if (value instanceof String && ((String) value).trim().isEmpty()) {
            // 空字符串
            return canBlank;
        }
        for (ValidEnum validEnum : validEnums) {
            if (validEnum.getValidValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
