package com.pandj.develop.plus.web.utils;

import com.pandj.develop.plus.web.bo.PageInfo;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 物理分页工具
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/8/16 16:14
 */
public class PageDataUtils<E> {
    /**
     * 分页
     *
     * @param dataCollection 数据
     * @param pageNum        当前页
     * @param pageSize       当前页数
     * @param <T>            数据类型
     * @return 分页数据
     */
    public static <T> PageInfo<T> structurePageInfo(Collection<T> dataCollection, Long pageNum, Long pageSize) {
        PageInfo<T> pageInfo = new PageInfo<>(pageNum, pageSize);
        if (!CollectionUtils.isEmpty(dataCollection)) {
            int totalNum = dataCollection.size();
            long skipNumber = (pageNum - 1L) * pageSize;
            if (skipNumber < totalNum) {
                pageInfo.setList(dataCollection.stream().skip(skipNumber).limit(pageSize).collect(Collectors.toList()));
            } else {
                pageInfo.setList(Collections.emptyList());
            }
            return pageInfo
                    .setTotal(totalNum)
                    .setPages(totalNum % pageSize > 0 ? (totalNum / pageSize) + 1 : totalNum / pageSize);
        } else {
            return pageInfo.getEmpty();
        }
    }
}
