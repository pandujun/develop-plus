package com.pandj.develop.plus.web.utils;

import cn.idev.excel.FastExcel;
import com.pandj.develop.plus.core.constant.ContentTypeConstant;
import com.pandj.develop.plus.core.result.ResultEnums;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    /**
     * 导出excel
     */
    public static void exportExcel(HttpServletResponse response, String fileName, Class<?> clazz, List<?> list) {
        response.setContentType(ContentTypeConstant.CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding(ContentTypeConstant.ENCODE_UTF_8);
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        try {
            FastExcel.write(response.getOutputStream(), clazz)
                    .sheet("sheet1")
                    .doWrite(list);
        } catch (IOException e) {
            logger.error("ExcelUtils#exportExcel ERROR：{ }", e);
            throw ResultEnums.WRITE_ERROR.getException();
        }
    }
}
