package com.pandj.develop.plus.web.utils;

import cn.idev.excel.FastExcel;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.util.ListUtils;
import com.pandj.develop.plus.core.constant.CommonSymbolConstant;
import com.pandj.develop.plus.core.constant.ContentTypeConstant;
import com.pandj.develop.plus.core.result.ResultEnums;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    private static final List<String> EXCEL_SUFFIX_LIST = List.of(".xls", ".xlsx");

    /**
     * 导出excel
     */
    public static void exportExcel(HttpServletResponse response, String fileName, Class<?> clazz, List<?> list, String sheetName) {
        response.setContentType(ContentTypeConstant.CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding(ContentTypeConstant.ENCODE_UTF_8);
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        try {
            FastExcel.write(response.getOutputStream(), clazz)
                    .sheet(sheetName)
                    .doWrite(CollectionUtils.isEmpty(list) ? Collections.emptyList() : list);
        } catch (IOException e) {
            logger.error("ExcelUtils#exportExcel ERROR：{ }", e);
            throw ResultEnums.WRITE_ERROR.getException();
        }
    }

    /**
     * 导出excel
     */
    public static void exportExcel(HttpServletResponse response, String fileName, Class<?> clazz, List<?> list) {
        exportExcel(response, fileName, clazz, list, "sheet1");
    }

    /**
     * 导入文件
     *
     * @param file      导入文件
     * @param clazz     导入文件的接收对象class
     * @param toSaveMap 接收对象class转换为保存的对象
     * @param consumer  保存方法
     * @param <T>       接收对象
     * @param <R>       保存对象
     */
    public static <T, R> void importExcel(MultipartFile file, Class<T> clazz, Function<T, R> toSaveMap, Consumer<List<R>> consumer) {
        //校验suffix
        checkSuffix(file);
        try {
            FastExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                /**
                 * 单次缓存的数据量
                 */
                public static final int BATCH_COUNT = 100;
                /**
                 *临时存储
                 */
                private List<R> saveList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

                @Override
                public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
                    checkImportExcelModel(headMap, clazz);
                }

                @Override
                public void invoke(T object, AnalysisContext analysisContext) {
                    R saveOne = toSaveMap.apply(object);
                    saveList.add(saveOne);
                    if (saveList.size() >= BATCH_COUNT) {
                        consumer.accept(saveList);
                        saveList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    consumer.accept(saveList);
                }
            }).sheet().doRead();
        } catch (IOException e) {
            logger.error("ExcelUtils#importExcel ERROR：{ }", e);
            throw ResultEnums.FILE_UPLOAD_ERROR.getException();
        }
    }

    /**
     * 导入文件
     *
     * @param file     导入文件
     * @param clazz    导入文件的接收对象class
     * @param consumer 保存方法
     * @param <T>      保存对象
     */
    public static <T> void importExcel(MultipartFile file, Class<T> clazz, Consumer<List<T>> consumer) {
        //校验suffix
        checkSuffix(file);
        try {
            FastExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                /**
                 * 单次缓存的数据量
                 */
                public static final int BATCH_COUNT = 100;
                /**
                 *临时存储
                 */
                private List<T> saveList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

                @Override
                public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
                    checkImportExcelModel(headMap, clazz);
                }

                @Override
                public void invoke(T object, AnalysisContext analysisContext) {
                    saveList.add(object);
                    if (saveList.size() >= BATCH_COUNT) {
                        consumer.accept(saveList);
                        saveList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                    consumer.accept(saveList);
                }
            }).sheet().doRead();
        } catch (IOException e) {
            logger.error("ExcelUtils#importExcel ERROR：{ }", e);
            throw ResultEnums.FILE_UPLOAD_ERROR.getException();
        }
    }

    /**
     * 校验文件是否存在和后缀名
     *
     * @param file 文件
     */
    private static void checkSuffix(MultipartFile file) {
        if (Objects.isNull(file)) {
            throw ResultEnums.DATA_DOES_NOT_EXIST.getException("传入的文件不能为空");
        }
        String filename = file.getOriginalFilename();
        String suffix = "无";
        if (Objects.nonNull(filename) && filename.contains(CommonSymbolConstant.DOT)) {
            suffix = filename.substring(filename.lastIndexOf(CommonSymbolConstant.DOT)).toLowerCase();
            if (EXCEL_SUFFIX_LIST.contains(suffix)) {
                return;
            }
        }
        throw ResultEnums.FILE_NOT_SUPPORT.getException(String.format("%s拓展名,系统不支持", suffix));
    }

    /**
     * 校验导入文件与模板是否一致
     *
     * @param headMap ReadListener中invokeHead方法的参数
     * @param clazz   模板对象
     */
    private static void checkImportExcelModel(Map<Integer, ReadCellData<?>> headMap, Class<?> clazz) {
        // 将读取到的表头按列顺序排序
        List<String> actualHead = headMap.values().stream()
                .map(ReadCellData::getStringValue)
                .toList();
        List<String> expectedHeaders = getExpectedHeaders(clazz);
        if (!actualHead.equals(expectedHeaders)) {
            throw ResultEnums.PARAM_ERROR.getException("当前Excel文件不是正确模版");
        }
    }

    /**
     * 获取导入的参数名称
     *
     * @param clazz 接收的导入参数名称对象
     * @return 参数名称
     */
    private static List<String> getExpectedHeaders(Class<?> clazz) {
        List<String> headers = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (annotation != null && annotation.value().length > 0) {
                // 取@ExcelProperty的第一个值作为表头
                headers.add(annotation.value()[0]);
            }
        }
        return headers;
    }

}
