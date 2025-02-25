package com.pandj.develop.plus.core.result;

/**
 * 返回参数异常抛出
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/13 11:16
 */
public interface ResultException {
    int code();

    String msg();

    default BusinessException getException() {
        return BusinessException.of(this.code(), this.msg());
    }

    default BusinessException getException(String msg) {
        return BusinessException.of(this.code(), msg);
    }

    default void throwE(String msg) {
        throw BusinessException.of(this.code(), msg);
    }

    default void throwE() {
        throw BusinessException.of(this.code(), this.msg());
    }
}
