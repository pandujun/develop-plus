package io.github.pandujun.develop.plus.core.utils.excel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义Excel列下拉列表属性的注解。
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSelected {
    /**
     * 方式一：固定的下拉选项
     */
    String[] source() default {};

    /**
     * 方式二：提供动态下拉选项的类
     */
    Class<? extends ExcelDynamicSelect>[] sourceClass() default {};

    /**
     * 下拉列表的起始行（默认从第二行开始）。
     */
    int firstRow() default 1;

    /**
     * 下拉列表的结束行（默认到第65536行）。
     */
    int lastRow() default 65536;
}

