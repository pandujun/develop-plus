package io.github.pandujun.develop.plus.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DateConvertUtils {
    private static final Logger logger = LoggerFactory.getLogger(DateConvertUtils.class);
    /**
     * 内部类，用于存储日期时间格式和对应的正则表达式
     */
    private static class DateTimePattern {
        final String pattern;
        final String regex;

        DateTimePattern(String pattern, String regex) {
            this.pattern = pattern;
            this.regex = regex;
        }
    }

    // 存储所有支持的日期时间格式
    private static final List<DateTimePattern> PATTERNS = new ArrayList<>();

    // 初始化常用格式
    static {
        // 日期时间格式
        addPattern("yyyy-MM-dd HH:mm:ss", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}");
        addPattern("yyyy/MM/dd HH:mm:ss", "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}");
        addPattern("yyyy-MM-dd HH:mm", "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
        addPattern("yyyy/MM/dd HH:mm", "\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}");
        addPattern("yyyyMMddHHmmss", "\\d{14}");
        addPattern("yyyyMMddHHmm", "\\d{12}");

        // 日期格式
        addPattern("yyyy-MM-dd", "\\d{4}-\\d{2}-\\d{2}");
        addPattern("yyyy/MM/dd", "\\d{4}/\\d{2}/\\d{2}");
        addPattern("yyyy.MM.dd", "\\d{4}\\.\\d{2}\\.\\d{2}");
        addPattern("dd-MM-yyyy", "\\d{2}-\\d{2}-\\d{4}");
        addPattern("dd/MM/yyyy", "\\d{2}/\\d{2}/\\d{4}");
        addPattern("MM/dd/yyyy", "\\d{2}/\\d{2}/\\d{4}");
        addPattern("yyyyMMdd", "\\d{8}");
        addPattern("ddMMyyyy", "\\d{8}");

        // 时间格式
        addPattern("HH:mm:ss", "\\d{2}:\\d{2}:\\d{2}");
        addPattern("HH:mm", "\\d{2}:\\d{2}");
        addPattern("HHmmss", "\\d{6}");
        addPattern("HHmm", "\\d{4}");
    }

    /**
     * 添加新的日期时间格式模式
     * @param pattern 日期时间格式
     * @param regex 匹配的正则表达式
     */
    public static void addPattern(String pattern, String regex) {
        PATTERNS.add(new DateTimePattern(pattern, regex));
    }

    /**
     * 转换日期时间字符串格式
     *
     * @param input 输入字符串
     * @param outputFormat 输出格式
     * @param returnNullOnError 转换失败时是否返回null
     * @return 转换后的字符串，失败时根据returnNullOnError参数返回null或原字符串
     */
    public static String convert(String input, String outputFormat, boolean returnNullOnError) {
        if (input == null || outputFormat == null) {
            logger.warn("输入参数不能为null: input={}, outputFormat={}", input, outputFormat);
            return returnNullOnError ? null : input;
        }

        input = input.trim();
        if (input.isEmpty()) {
            logger.warn("输入字符串为空");
            return returnNullOnError ? null : input;
        }

        try {
            // 尝试自动识别输入格式
            DateTimePattern detectedPattern = detectPattern(input);
            if (detectedPattern == null) {
                logger.warn("无法识别的日期时间格式: {}", input);
                return returnNullOnError ? null : input;
            }

            // 解析输入字符串
            Object parsedValue = parseInput(input, detectedPattern.pattern);

            // 格式化为目标格式
            return formatOutput(parsedValue, outputFormat);
        } catch (Exception e) {
            logger.warn("日期时间格式转换失败: input={}, outputFormat={}, error={}",
                    input, outputFormat, e.getMessage());
            return returnNullOnError ? null : input;
        }
    }

    /**
     * 自动检测输入字符串的格式
     */
    private static DateTimePattern detectPattern(String input) {
        for (DateTimePattern pattern : PATTERNS) {
            if (Pattern.matches(pattern.regex, input)) {
                return pattern;
            }
        }
        return null;
    }

    /**
     * 解析输入字符串
     */
    private static Object parseInput(String input, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 尝试解析为LocalDateTime
        try {
            return LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            // 忽略，继续尝试其他类型
        }

        // 尝试解析为LocalDate
        try {
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            // 忽略，继续尝试其他类型
        }

        // 尝试解析为LocalTime
        try {
            return LocalTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            // 忽略
        }

        throw new DateTimeParseException("无法解析日期时间字符串", input, 0);
    }

    /**
     * 格式化输出
     */
    private static String formatOutput(Object value, String outputFormat) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);

        if (value instanceof LocalDateTime) {
            return ((LocalDateTime) value).format(outputFormatter);
        } else if (value instanceof LocalDate) {
            LocalDate date = (LocalDate) value;
            // 日期转换为时间格式时，自动补全为00:00:00
            if (outputFormat.contains("HH") || outputFormat.contains("mm") || outputFormat.contains("ss")) {
                return date.atStartOfDay().format(outputFormatter);
            }
            return date.format(outputFormatter);
        } else if (value instanceof LocalTime) {
            LocalTime time = (LocalTime) value;
            // 时间转换为日期格式时，使用当前日期
            if (outputFormat.contains("yyyy") || outputFormat.contains("MM") || outputFormat.contains("dd")) {
                return LocalDate.now().atTime(time).format(outputFormatter);
            }
            return time.format(outputFormatter);
        }

        throw new IllegalArgumentException("不支持的日期时间类型: " + value.getClass());
    }
}
