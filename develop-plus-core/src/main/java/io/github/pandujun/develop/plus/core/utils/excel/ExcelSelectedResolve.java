package io.github.pandujun.develop.plus.core.utils.excel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据 ExcelSelected 注解解析下拉列表数据源。
 */
public class ExcelSelectedResolve {
    private static Logger logger = LoggerFactory.getLogger(ExcelSelectedResolve.class);
    /**
     * 下拉选项数组。
     */
    private String[] source;

    /**
     * 下拉列表的起始行。
     */
    private int firstRow;

    /**
     * 下拉列表的结束行。
     */
    private int lastRow;

    /**
     * 解析下拉列表数据来源
     *
     * @param excelSelected 下拉框注解对象
     * @return 下拉框选项数组
     */
    public String[] resolveSelectedSource(ExcelSelected excelSelected) {
        if (excelSelected == null) {
            return null;
        }

        // 方式一：获取固定下拉框的内容
        String[] source = excelSelected.source();
        if (source.length > 0) {
            return source;
        }

        // 方式二：获取动态下拉框的内容
        Class<? extends ExcelDynamicSelect>[] classes = excelSelected.sourceClass();
        if (classes.length > 0) {
            try {
                return classes[0].getEnumConstants()[0].getEnumSource();
            } catch (Exception e) {
                logger.error("解析动态下拉框数据异常:{ }", e);
            }
        }

        return null;
    }

    public String[] getSource() {
        return source;
    }

    public void setSource(String[] source) {
        this.source = source;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

}
