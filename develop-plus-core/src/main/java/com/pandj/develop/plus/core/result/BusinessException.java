package com.pandj.develop.plus.core.result;

/**
 * 业务异常
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/13 11:17
 */
public class BusinessException extends RuntimeException {
    private final int code;
    private final String msg;

    protected BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }


    public static BusinessException of(int code, String msg) {
        return new BusinessException(code, msg);
    }

}
