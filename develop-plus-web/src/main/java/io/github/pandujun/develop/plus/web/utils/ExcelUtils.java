package io.github.pandujun.develop.plus.web.utils;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.FastExcel;
import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.read.listener.ReadListener;
import cn.idev.excel.util.ListUtils;
import cn.idev.excel.write.builder.ExcelWriterSheetBuilder;
import io.github.pandujun.develop.plus.core.constant.CommonSymbolConstant;
import io.github.pandujun.develop.plus.core.constant.ContentTypeConstant;
import io.github.pandujun.develop.plus.core.constant.FileSuffixConstant;
import io.github.pandujun.develop.plus.core.constant.NumberConstant;
import io.github.pandujun.develop.plus.core.result.ResultEnums;
import io.github.pandujun.develop.plus.web.bo.ExcelExportInfoBO;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
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
    private static final String SUFFIX_EXCEL_XLSX = CommonSymbolConstant.DOT + FileSuffixConstant.EXCEL_XLSX;
    private static final List<String> EXCEL_SUFFIX_LIST = List.of(
            CommonSymbolConstant.DOT + FileSuffixConstant.EXCEL_XLS,
            SUFFIX_EXCEL_XLSX);
    private static final String DEFAULT_SHEET_NAME = "sheet";


    public static void exportExcel(HttpServletResponse response, String fileName, @Validated ExcelExportInfoBO excelExportInfoBO) {
        response.setContentType(ContentTypeConstant.CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding(ContentTypeConstant.ENCODE_UTF_8);
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader(ContentTypeConstant.HEADER_CONTENT_DISPOSITION, "attachment;filename=" + fileName + SUFFIX_EXCEL_XLSX);

        try {
            ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel.write(response.getOutputStream(), excelExportInfoBO.getClazz())
                    .sheet(StringUtils.hasLength(excelExportInfoBO.getSheetName()) ? excelExportInfoBO.getSheetName() : DEFAULT_SHEET_NAME);
            if (Objects.nonNull(excelExportInfoBO.getWriteHandler())) {
                excelWriterSheetBuilder.registerWriteHandler(excelExportInfoBO.getWriteHandler());
            }
            excelWriterSheetBuilder.doWrite(CollectionUtils.isEmpty(excelExportInfoBO.getList()) ? Collections.emptyList() : excelExportInfoBO.getList());
        } catch (IOException e) {
            logger.error("ExcelUtils#exportExcel ERROR：", e);
            throw ResultEnums.WRITE_ERROR.getException();
        }
    }

    public static void exportExcelBatch(HttpServletResponse response, String fileName, @Validated List<ExcelExportInfoBO> excelExportInfoBOList) {
        response.setContentType(ContentTypeConstant.CONTENT_TYPE_EXCEL);
        response.setCharacterEncoding(ContentTypeConstant.ENCODE_UTF_8);
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader(ContentTypeConstant.HEADER_CONTENT_DISPOSITION, "attachment;filename=" + fileName + SUFFIX_EXCEL_XLSX);

        try {
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            for (int i = 0; i < excelExportInfoBOList.size(); i++) {
                ExcelExportInfoBO excelExportInfoBO = excelExportInfoBOList.get(i);
                String sheetName = StringUtils.hasLength(excelExportInfoBO.getSheetName()) ? excelExportInfoBO.getSheetName() : (DEFAULT_SHEET_NAME + (i + 1));
                List<?> list = CollectionUtils.isEmpty(excelExportInfoBO.getList()) ? Collections.emptyList() : excelExportInfoBO.getList();
                ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel
                        .writerSheet(0, sheetName)
                        .head(excelExportInfoBO.getClazz());
                if (Objects.nonNull(excelExportInfoBO.getWriteHandler())) {
                    excelWriterSheetBuilder.registerWriteHandler(excelExportInfoBO.getWriteHandler());
                }
                excelWriter.write(list, excelWriterSheetBuilder.build());
            }
            excelWriter.finish();
        } catch (IOException e) {
            logger.error("ExcelUtils#exportExcelBath ERROR：", e);
            throw ResultEnums.WRITE_ERROR.getException();
        }
    }

    /**
     * 导入文件
     *
     * @param file              导入文件
     * @param clazz             导入文件的接收对象class
     * @param convertFunction   接收对象class转换为保存的对象
     * @param consumer          保存方法
     * @param <T>               接收对象
     * @param <R>               保存对象
     */
    public static <T, R> void importExcel(MultipartFile file, Class<T> clazz, Function<T, R> convertFunction, Consumer<List<R>> consumer) {
        //校验suffix
        checkSuffix(file);
        try {
            FastExcel.read(file.getInputStream(), clazz, new ReadListener<T>() {
                /**
                 * 单次缓存的数据量
                 */
                public static final int BATCH_COUNT = NumberConstant.HUNDRED_NUM;
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
                    R saveOne = convertFunction.apply(object);
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
            logger.error("ExcelUtils#importExcel ERROR：", e);
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
                public static final int BATCH_COUNT = NumberConstant.HUNDRED_NUM;
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
            logger.error("ExcelUtils#importExcel ERROR：", e);
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
            throw ResultEnums.DATA_NOT_EXIST.getException("传入的文件不能为空");
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
            if (annotation != null && annotation.value().length > NumberConstant.ZERO_NUM) {
                // 取@ExcelProperty的第一个值作为表头
                headers.add(annotation.value()[NumberConstant.ZERO_NUM]);
            }
        }
        return headers;
    }

}
