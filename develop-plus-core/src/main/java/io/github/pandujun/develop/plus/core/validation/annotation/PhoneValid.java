package io.github.pandujun.develop.plus.core.validation.annotation;

import io.github.pandujun.develop.plus.core.validation.validator.PhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
@Documented
public @interface PhoneValid {
    /**
     * 校验分组（可选）
     */
    Class<?>[] groups() default {};

    /**
     * 负载信息（可选）
     */
    Class<? extends Payload>[] payload() default {};


    /**
     * 错误提示消息（必填）
     */
    String message() default "手机号码格式无效";

    /**
     * 是否可以为空（默认为false）
     */
    boolean canNull() default false;
}
