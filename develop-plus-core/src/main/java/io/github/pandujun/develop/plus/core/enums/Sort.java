package io.github.pandujun.develop.plus.core.enums;

import io.github.pandujun.develop.plus.core.validation.annotation.ValidEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 排序规则
 */
public enum Sort implements ValidEnum {
    DESC("DESC", "倒序"),
    ASC("ASC", "升序");
    private final String code;
    private final String description;
    private static final Map<String, Sort> codeMap = new HashMap<>();
    static {
        for (Sort sort : values()) {
            codeMap.put(sort.code, sort);
        }
    }

    Sort(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static Sort getByCode(String code) {
        return codeMap.get(code);
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Object getValidValue() {
        return code;
    }
}
