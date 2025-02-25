package com.pandj.develop.plus.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动结果封装
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/30 10:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoPackaging {

    /**
     * 是否自动封装返回值Result
     */
    boolean value() default true;
}
