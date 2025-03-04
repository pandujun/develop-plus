package com.pandj.develop.plus.core.validation.validator;

import com.pandj.develop.plus.core.validation.annotation.PhoneValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<PhoneValid, String> {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private boolean canNull;
    @Override
    public void initialize(PhoneValid constraintAnnotation) {
        this.canNull = constraintAnnotation.canNull();
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        if (phone == null) {
            return canNull; // 结合 @NotNull 使用，此处无需处理 null
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

}
