package io.github.pandujun.develop.plus.core.utils;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 时间相关的工具类
 */
public class DateUtils {
    /**
     * 获取现在到明天之间相差的 秒数
     *
     * @return 秒数
     */
    public static Long getNextDayLong() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowMidnight = now.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        // 计算时间差（秒）
        return Duration.between(now, tomorrowMidnight).getSeconds();
    }
}
