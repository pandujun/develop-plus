package io.github.pandujun.develop.plus.web.bo;

import cn.idev.excel.write.handler.WriteHandler;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ExcelExportInfoBO {
    /**
     * 导出对象
     */
    @NotNull(message = "导出对象class不能为空")
    private Class<?> clazz;

    /**
     * 导出数据
     */
    private List<?> list;

    /**
     * sheetName
     */
    private String sheetName;

    /**
     * 写入拦截
     */
    private WriteHandler writeHandler;

    public Class<?> getClazz() {
        return clazz;
    }

    public ExcelExportInfoBO setClazz(Class<?> clazz) {
        this.clazz = clazz;
        return this;
    }

    public List<?> getList() {
        return list;
    }

    public ExcelExportInfoBO setList(List<?> list) {
        this.list = list;
        return this;
    }

    public String getSheetName() {
        return sheetName;
    }

    public ExcelExportInfoBO setSheetName(String sheetName) {
        this.sheetName = sheetName;
        return this;
    }

    public WriteHandler getWriteHandler() {
        return writeHandler;
    }

    public ExcelExportInfoBO setWriteHandler(WriteHandler writeHandler) {
        this.writeHandler = writeHandler;
        return this;
    }
}
