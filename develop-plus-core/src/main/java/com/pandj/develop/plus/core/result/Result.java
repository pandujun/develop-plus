package com.pandj.develop.plus.core.result;

/**
 * 全局返回数据格式
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/13 11:33
 */
public class Result<E> {
    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码对应含义，或自定义含义
     */
    private final String msg;

    /**
     * 返回数据
     */
    private final E data;

    public static <E> Result<E> success(E data) {
        return new Result<>(ResultEnums.SUCCESS.code(), ResultEnums.SUCCESS.msg(), data);
    }

    public static Result<Object> success() {
        return new Result<>(ResultEnums.SUCCESS.code(), ResultEnums.SUCCESS.msg(), null);
    }

    public static <E> Result<E> error(ResultEnums resultType, String msg) {
        return new Result<>(resultType.code(), msg);
    }

    public static <E> Result<E> error(ResultEnums resultType) {
        return new Result<>(resultType.code(), resultType.msg());
    }


    public Result(int code, String msg, E data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public E getData() {
        return data;
    }

}
