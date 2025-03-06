package io.github.pandujun.develop.plus.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析数据相关
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2024/7/25
 */
public class AnalysisUtils {
    private static final Logger logger = LoggerFactory.getLogger(AnalysisUtils.class);

    /**
     * 解析类字段和数据(默认不带前后缀)
     *
     * @return key:字段名；value:字段名对应数据
     */
    public static Map<String, String> analysisClassParam(Object obj) {
        return analysisClassParam(obj, "", "");
    }

    /**
     * 解析类字段和数据
     *
     * @param obj         任何对象
     * @param firstPrefix 前缀
     * @param endPrefix   后缀
     * @return key:前缀+字段名+后缀；value:字段名对应数据
     */
    public static Map<String, String> analysisClassParam(Object obj, String firstPrefix, String endPrefix) {
        Map<String, String> map = new HashMap<>();
        if (obj == null) {
            return map;
        }

        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true); // 确保可以访问私有字段
                Object value = field.get(obj);
                String key = firstPrefix + field.getName() + endPrefix;
                map.put(key, (value != null) ? value.toString() : "");
            } catch (IllegalAccessException e) {
                logger.error("AnalysisUtils#analysisClassParam ERROR：{ }", e);
                // 处理异常，例如记录日志或返回空Map
                return Collections.emptyMap();
            }
        }
        return map;
    }
}
