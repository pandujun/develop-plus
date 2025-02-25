package com.pandj.develop.plus.web.annotation;

import com.pandj.develop.plus.web.annotation.validator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
@Documented
@Repeatable(EnumValid.List.class)
public @interface EnumValid {

    /**
     * 枚举类实现该接口
     */
    Class<? extends ValidEnum> value();

    String message() default "enum valid fail";

    /**
     * 是否可以为空
     */
    boolean canNull() default false;

    /**
     * String类型下是否可以为空字符串
     */
    boolean canBlank() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EnumValid[] value();
    }
}
