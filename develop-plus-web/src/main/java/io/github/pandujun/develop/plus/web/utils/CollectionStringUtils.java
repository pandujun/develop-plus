package io.github.pandujun.develop.plus.web.utils;

import io.github.pandujun.develop.plus.core.constant.CommonSymbolConstant;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 集合与字符串转换工具类
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/27 15:31
 */
public class CollectionStringUtils {

    /**
     * 字符串截取成list集合(默认,分隔）
     *
     * @param text 字符串
     * @return list集合
     */
    public static List<String> toList(String text) {
        return toList(text, CommonSymbolConstant.COMMA_E);
    }

    /**
     * 字符串截取成list集合
     *
     * @param text      字符串
     * @param delimiter 分隔符
     * @return list集合
     */
    public static List<String> toList(String text, String delimiter) {
        return StringUtils.hasText(text) ? Arrays.stream(text.split(delimiter)).collect(Collectors.toList()) : Collections.emptyList();
    }

    /**
     * 字符串截取成set集合(默认,分隔）
     *
     * @param text 字符串
     * @return set集合
     */
    public static Set<String> toSet(String text) {
        return toSet(text, CommonSymbolConstant.COMMA_E);
    }


    /**
     * 字符串截取成set集合
     *
     * @param text      字符串
     * @param delimiter 分隔符
     * @return set集合
     */
    public static Set<String> toSet(String text, String delimiter) {
        return StringUtils.hasText(text) ? Arrays.stream(text.split(delimiter)).collect(Collectors.toSet()) : Collections.emptySet();
    }

    /**
     * 集合转成字符串
     *
     * @param collection 集合
     * @return 返回字符串
     */
    public static <E> String toStr(Collection<E> collection) {
        return toStr(collection, CommonSymbolConstant.COMMA_E);
    }

    /**
     * 集合转成字符串
     *
     * @param collection 集合
     * @param delimiter  分隔符
     * @return 返回字符串
     */
    public static <E> String toStr(Collection<E> collection, String delimiter) {
        return !CollectionUtils.isEmpty(collection) ? collection.stream().map(String::valueOf).collect(Collectors.joining(delimiter)) : "";
    }

}
