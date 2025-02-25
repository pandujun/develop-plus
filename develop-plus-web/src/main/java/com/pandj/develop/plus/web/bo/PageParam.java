package com.pandj.develop.plus.web.bo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * 分页入参
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/11/15 14:55
 */
public class PageParam<T extends PageParam<T>> implements Serializable {

    @NotNull(message = "Num Not Null")
    @Min(value = 1, message = "Num Min 1")
    protected Integer pageNum;

    @NotNull(message = "Size Not Null")
    @Size(min = 1, max = 100, message = "Size Between 1-100")
    protected Integer pageSize;

    public Integer getPageNum() {
        return pageNum;
    }

    public T setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
        return (T) this;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public T setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return (T) this;
    }
}
