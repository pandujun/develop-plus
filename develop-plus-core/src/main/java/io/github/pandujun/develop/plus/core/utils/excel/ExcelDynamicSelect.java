package io.github.pandujun.develop.plus.core.utils.excel;

/**
 * 动态下拉列表数据提供者接口。
 */
public interface ExcelDynamicSelect {
    /**
     * 获取动态生成的下拉列表选项。
     * 
     * @return 下拉选项数组。
     */
    String[] getEnumSource();
}
