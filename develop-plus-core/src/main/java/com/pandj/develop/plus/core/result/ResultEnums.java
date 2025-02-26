package com.pandj.develop.plus.core.result;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局返回参数枚举
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/13 11:08
 */
public enum ResultEnums implements ResultException {
    SUCCESS(200, "SUCCESS"),
    NO_PERMISSION(401, "无权限"),
    URL_NOT_FOUND(404, "无效的访问地址"),
    TOO_MANY_REQUESTS(429, "系统繁忙,请稍后再试"),
    INTERNAL_SERVER_ERROR(500, "内部错误,请稍后再试"),
    PARAM_ERROR(601, "参数异常"),
    DATA_DOES_NOT_EXIST(602, "数据不存在"),
    DATA_IS_EXIST(603, "数据已存在,请勿重复"),
    FILE_NOT_SUPPORT(604, "不支持的文件格式"),
    FILE_UPLOAD_ERROR(605, "文件上传失败,请稍后再试"),
    EXISTS_USED_REMOVE_ILLEGAL(606, "存在使用,删除失败"),
    EXISTS_USED_DISABLE_ILLEGAL(607, "存在使用,禁用失败"),
    ENABLE_REMOVE_ILLEGAL(608, "启用中,删除失败"),
    ENABLE_UPDATE_ILLEGAL(609, "启用中,不可编辑"),
    SECURE_ERROR(610, "加解密失败"),
    CHANGE_ERROR(611, "转换失败"),
    WRITE_ERROR(612, "写入失败"),
    HTTP_ERROR(700, "请求异常");

    private final int code;
    private final String msg;
    private static final Map<Integer, ResultEnums> map;

    static {
        map = new HashMap<>(values().length);
        for (ResultEnums resultEnum : values()) {
            map.put(resultEnum.code, resultEnum);
        }
    }

    ResultEnums(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }

    public static ResultEnums getMapCode(Integer code) {
        return map.get(code);
    }
}
