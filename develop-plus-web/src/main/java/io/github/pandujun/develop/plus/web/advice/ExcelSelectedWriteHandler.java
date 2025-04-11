package io.github.pandujun.develop.plus.web.advice;

import cn.idev.excel.annotation.ExcelProperty;
import cn.idev.excel.write.handler.SheetWriteHandler;
import cn.idev.excel.write.metadata.holder.WriteSheetHolder;
import cn.idev.excel.write.metadata.holder.WriteWorkbookHolder;
import io.github.pandujun.develop.plus.core.utils.excel.ExcelSelected;
import io.github.pandujun.develop.plus.core.utils.excel.ExcelSelectedResolve;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理Excel下拉列表的SheetWriteHandler实现类。
 */
public class ExcelSelectedWriteHandler implements SheetWriteHandler {
    // 存储列索引与对应下拉列表解析器的映射
    private Map<Integer, ExcelSelectedResolve> selectedMap = new HashMap<>();

    /**
     * 构造方法，解析表头类中的下拉列表注解信息。
     *
     * @param head 表头类。
     */
    public ExcelSelectedWriteHandler(Class<?> head) {
        // 获取所有声明的字段
        Field[] fields = head.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            // 获取 ExcelSelected 注解
            ExcelSelected selected = field.getAnnotation(ExcelSelected.class);
            ExcelProperty property = field.getAnnotation(ExcelProperty.class);
            if (selected != null) {
                ExcelSelectedResolve resolve = new ExcelSelectedResolve();
                // 解析下拉列表数据源
                String[] source = resolve.resolveSelectedSource(selected);
                if (source != null && source.length > 0) {
                    resolve.setSource(source);
                    resolve.setFirstRow(selected.firstRow());
                    resolve.setLastRow(selected.lastRow());
                    // 使用注解中的索引或字段顺序作为列索引
                    if (property != null && property.index() >= 0) {
                        selectedMap.put(property.index(), resolve);
                    } else {
                        selectedMap.put(i, resolve);
                    }
                }
            }
        }
    }

    /**
     * 在创建Sheet之前调用的方法。
     */
    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        // 此处无需操作，保持空实现
    }

    /**
     * 在Sheet创建后调用的方法，用于设置Excel下拉列表。
     *
     * @param writeWorkbookHolder 写入的工作簿持有者。
     * @param writeSheetHolder    写入的Sheet持有者。
     */
    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = sheet.getWorkbook();
        // SXSSFWorkbook 是 Apache POI 库中用于处理大文件的一种特殊工作簿类型
        SXSSFWorkbook sw = (SXSSFWorkbook) workbook;
        // 1.创建一个隐藏的sheet，名称为hidden，用于存储下拉列表选项
        String hiddenName = "hidden";
        XSSFSheet hiddenSheet = sw.getXSSFWorkbook().createSheet(hiddenName);
        // 将隐藏的sheet设置为不可见
        workbook.setSheetHidden(workbook.getSheetIndex(hiddenName), true);
        // 创建数据验证辅助器
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 为每个需要下拉列表的列创建数据验证
        selectedMap.forEach((index, selectedResolve) -> {
            // 设置下拉列表的范围：起始行，结束行，起始列，结束列
            CellRangeAddressList rangeList = new CellRangeAddressList(
                    selectedResolve.getFirstRow(),
                    selectedResolve.getLastRow(),
                    index,
                    index
            );
            // 在隐藏的sheet中生成下拉列表选项值
            String[] values = selectedResolve.getSource();
            generateSelectValue(hiddenSheet, index, values);
            // 获取Excel列标，例如A, B, AA
            String excelLine = getExcelLine(index);
            // 引用隐藏sheet中的单元格区域，例如hidden!$H$1:$H$50
            String refers = hiddenName + "!$" + excelLine + "$1:$" + excelLine + "$" + values.length;
            // 使用引用的内容作为下拉列表的值
            DataValidationConstraint constraint = helper.createFormulaListConstraint(refers);
            DataValidation validation = helper.createValidation(constraint, rangeList);

            // 设置验证属性，阻止输入非下拉选项的值
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.setShowErrorBox(true);
            validation.setSuppressDropDownArrow(true);
            validation.createErrorBox("提示", "请输入下拉选项中的内容");
            // 将验证添加到当前的sheet中
            sheet.addValidationData(validation);
        });
    }

    /**
     * 获取Excel列标（例如：A-Z, AA-ZZ）。
     *
     * @param num 列索引，从0开始。
     * @return Excel列标字符串。
     */
    public static String getExcelLine(int num) {
        StringBuilder line = new StringBuilder();
        // 计算列标，使用字母表示，例如 A, B, ..., Z, AA, AB, ...
        int first = num / 26;
        int second = num % 26;
        if (first > 0) {
            line.append((char) ('A' + first - 1));
        }
        line.append((char) ('A' + second));
        return line.toString();
    }

    /**
     * 在隐藏的sheet中生成下拉列表选项值。
     *
     * @param sheet  隐藏的sheet对象。
     * @param col    列索引。
     * @param values 下拉列表选项值数组。
     */
    private void generateSelectValue(Sheet sheet, int col, String[] values) {
        // 将下拉列表选项值写入隐藏的sheet中，每个选项值占用一行
        for (int i = 0, length = values.length; i < length; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            // 在指定列中创建单元格并设置下拉列表选项值
            row.createCell(col).setCellValue(values[i]);
        }
    }

}
