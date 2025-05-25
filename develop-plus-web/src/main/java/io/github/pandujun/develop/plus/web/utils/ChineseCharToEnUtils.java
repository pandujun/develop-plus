package io.github.pandujun.develop.plus.web.utils;

import io.github.pandujun.develop.plus.core.constant.CommonSymbolConstant;
import io.github.pandujun.develop.plus.core.constant.NumberConstant;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 名字首字母转换
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2024/3/7
 */
public class ChineseCharToEnUtils {
    private final static List<String> LC_FIRST_LETTER = Arrays.asList("a", "b", "c", "d", "e",
            "f", "g", "h", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "w", "x", "y", "z");

    /**
     * 提取汉字的首字母(小写)
     *
     * @param str 中文汉字
     * @return 英文首字母
     */
    public static String getPinYinHeadChar(String str) {
        if (!StringUtils.hasText(str)) {
            return CommonSymbolConstant.EMPTY_STR;
        }
        String convert;
        char word = str.charAt(NumberConstant.ZERO_NUM);
        // 提取汉字的首字母
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
        if (pinyinArray != null) {
            convert = String.valueOf(pinyinArray[NumberConstant.ZERO_NUM].charAt(NumberConstant.ZERO_NUM));
        } else {
            convert = String.valueOf(word);
        }

        convert = string2AllTrim(convert).toLowerCase();
        return LC_FIRST_LETTER.contains(convert) ? convert : CommonSymbolConstant.JING;
    }

    /**
     * 去掉字符串包含的所有空格
     */
    private static String string2AllTrim(String value) {
        if (!StringUtils.hasText(value)) {
            return CommonSymbolConstant.EMPTY_STR;
        }
        return value.replace(CommonSymbolConstant.SPACE, CommonSymbolConstant.EMPTY_STR);
    }

}
