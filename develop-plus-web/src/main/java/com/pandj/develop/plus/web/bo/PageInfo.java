package com.pandj.develop.plus.web.bo;

import java.util.Collections;
import java.util.List;

/**
 * 分页数据封装
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/10/27 14:08
 */
public class PageInfo<T> {

    /**
     * 页码，从1开始
     */
    private long pageNum;
    /**
     * 每页数据量
     */
    private long pageSize;
    /**
     * 总数据量
     */
    private long total;
    /**
     * 总页数
     */
    private long pages;
    /**
     * 结果集
     */
    private List<T> list;

    public PageInfo() {
    }

    public PageInfo(Long pageNum, Long pageSize) {
        this.pageNum = pageNum != null ? pageNum : 1;
        this.pageSize = pageSize != null ? pageSize : 20;
    }

    public PageInfo<T> getEmpty() {
        this.total = 0;
        this.pages = 0;
        this.list = Collections.emptyList();
        return this;
    }

    public long getPageNum() {
        return pageNum;
    }

    public PageInfo<T> setPageNum(long pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public long getPageSize() {
        return pageSize;
    }

    public PageInfo<T> setPageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageInfo<T> setTotal(long total) {
        this.total = total;
        return this;
    }

    public long getPages() {
        return pages;
    }

    public PageInfo<T> setPages(long pages) {
        this.pages = pages;
        return this;
    }

    public List<T> getList() {
        return list;
    }

    public PageInfo<T> setList(List<T> list) {
        this.list = list;
        return this;
    }
}
