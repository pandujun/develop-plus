package com.pandj.develop.plus.core.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 时间格式相关
 */
public class DatePatternConstant {
    public static final String TIME_ZONE_AREA = "Asia/Shanghai";
    public static final String TIME_ZONE_GMT = "GMT+8";

    public static final String Y_M_D_H_M_S_NORMAL = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter Y_M_D_H_M_S_NORMAL_PATTERN = createFormatter(Y_M_D_H_M_S_NORMAL);

    public static final String Y_M_D_H_M_S_SLASH = "yyyy/MM/dd HH:mm:ss";
    public static final DateTimeFormatter Y_M_D_H_M_S_SLASH_PATTERN = createFormatter(Y_M_D_H_M_S_SLASH);

    public static final String Y_M_D_NORMAL = "yyyy-MM-dd";
    public static final DateTimeFormatter Y_M_D_NORMAL_PATTERN = createFormatter(Y_M_D_NORMAL);

    public static final String Y_M_D_SLASH = "yyyy/MM/dd";
    public static final DateTimeFormatter Y_M_D_SLASH_PATTERN = createFormatter(Y_M_D_SLASH);

    public static final String H_M_S_NORMAL = "HH:mm:ss";
    public static final DateTimeFormatter H_M_S_NORMAL_PATTERN = createFormatter(H_M_S_NORMAL);

    public static final String Y_M_D_H_M_S_ONLY = "yyyyMMddHHmmss";
    public static final DateTimeFormatter Y_M_D_H_M_S_ONLY_PATTERN = createFormatter(Y_M_D_H_M_S_ONLY);

    public static final String Y_M_D_ONLY = "yyyyMMdd";
    public static final DateTimeFormatter Y_M_D_ONLY_PATTERN = createFormatter(Y_M_D_ONLY);

    /**
     * 创建一个DateTimeFormatter
     *
     * @param pattern DatePatternConstant当中常量数据
     * @return DateTimeFormatter
     */
    public static DateTimeFormatter createFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern, Locale.getDefault()).withZone(ZoneId.of(TIME_ZONE_AREA));
    }
}
