package top.ibase4j.core.util;

import top.ibase4j.core.support.file.excel.WriteDone;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author ShenHuaJie
 * @since 2019年4月4日 下午3:01:27
 */
public class ExcelWriterPoiUtil {
    private static final Logger logger = LogManager.getLogger();

    /**
     * 设置文件名
     * @param request
     * @param response
     * @param fileName
     * @throws UnsupportedEncodingException
     */
    public static void setResponseFileName(HttpServletRequest request, HttpServletResponse response, String fileName)
            throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");
        boolean isIE = false;
        if (userAgent != null && (userAgent.toLowerCase().contains("msie") || userAgent.toLowerCase().contains("rv"))) {
            isIE = true;
        }
        String displayName;
        if (isIE) {
            displayName = URLEncoder.encode(fileName, "UTF-8");
            displayName = displayName.replaceAll("\\+", "%20");// 修正URLEncoder将空格转换成+号的BUG
            response.setHeader("Content-Disposition", "attachment;filename=" + displayName);
        } else {
            displayName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + displayName + "\"");// firefox空格截断
        }
        String extStr = displayName.substring(displayName.indexOf(".") + 1);
        if ("xls".equalsIgnoreCase(extStr)) {
            response.setContentType("application/vnd.ms-excel");
        } else {
            response.setContentType("application/octet-stream");
        }
    }

    /**
     * 生成带下拉框Excel导入模板
     * @param @param filePath  Excel文件路径
     * @param @param handers   Excel列标题(数组)
     * @param @param downData  下拉框数据(数组)
     * @param @param downRows  下拉列的序号(数组,序号从0开始)
     * @return void
     * @throws
     */
    public static void createExcelTemplate(String filePath, String[] handers, List<String[]> downData,
        String[] downRows) throws RuntimeException {
        OutputStream out = null;
        try (HSSFWorkbook wb = new HSSFWorkbook()) {// 创建工作薄
            out = writeTemplate(filePath, handers, downData, downRows, wb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("关闭输出流异常", e);
                }
            }
        }
    }

    /**
     * 生成带下拉框Excel导入模板
     * @param @param filePath  Excel文件路径
     * @param @param handers   Excel列标题(数组)
     * @param @param downData  下拉框数据(数组)
     * @param @param downRows  下拉列的序号(数组,序号从0开始)
     * @return void
     * @throws
     */
    public static void createExcelTemplate(URL tplPath, String filePath, String[] handers, List<String[]> downData,
        String[] downRows) throws RuntimeException {
        OutputStream out = null;
        try (HSSFWorkbook wb = new HSSFWorkbook(tplPath.openStream())) {// 创建工作薄
            out = writeTemplate(filePath, handers, downData, downRows, wb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("关闭输出流异常", e);
                }
            }
        }
    }

    private static OutputStream writeTemplate(String filePath, String[] handers, List<String[]> downData,
        String[] downRows, HSSFWorkbook wb) throws IOException, FileNotFoundException {
        OutputStream out;
        // 表头样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        // 字体样式
        HSSFFont fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short)12);
        fontStyle.setBold(true);
        style.setFont(fontStyle);

        // 新建sheet
        HSSFSheet sheet1 = wb.createSheet("Sheet1");
        HSSFSheet sheet2 = wb.createSheet("Sheet2");

        // 生成sheet1内容
        HSSFRow rowFirst = sheet1.createRow(0);// 第一个sheet的第一行为标题
        // 写标题
        for (int i = 0; i < handers.length; i++) {
            HSSFCell cell = rowFirst.createCell(i); // 获取第一行的每个单元格
            sheet1.setColumnWidth(i, 4000); // 设置每列的列宽
            cell.setCellStyle(style); // 加样式
            cell.setCellValue(handers[i]); // 往单元格里写数据
        }

        // 设置下拉框数据
        String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};
        int index = 0;
        HSSFRow row = null;
        for (int r = 0; r < downRows.length; r++) {
            String[] dlData = downData.get(r);// 获取下拉对象
            int rownum = Integer.parseInt(downRows[r]);

            if (dlData.length < 5) { // 255以内的下拉
                // 255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                sheet1.addValidationData(setDataValidation(sheet1, dlData, 1, 5, rownum, rownum)); // 超过255个报错
            } else { // 255以上的下拉，即下拉列表元素很多的情况
                // 1、设置有效性
                // String strFormula = "Sheet2!$A$1:$A$5000" ; //Sheet2第A1到A5000作为下拉列表来源数据
                String strFormula = "Sheet2!$" + arr[index] + "$1:$" + arr[index] + "$5000"; // Sheet2第A1到A5000作为下拉列表来源数据
                sheet2.setColumnWidth(r, 4000); // 设置每列的列宽
                // 设置数据有效性加载在哪个单元格上,参数分别是：从sheet2获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                sheet1.addValidationData(setDataValidation(strFormula, 1, 50000, rownum, rownum)); // 下拉列表元素很多的情况

                // 2、生成sheet2内容
                for (int j = 0; j < dlData.length; j++) {
                    if (index == 0) { // 第1个下拉选项，直接创建行、列
                        row = sheet2.createRow(j); // 创建数据行
                        sheet2.setColumnWidth(j, 4000); // 设置每列的列宽
                        row.createCell(0).setCellValue(dlData[j]); // 设置对应单元格的值
                    } else { // 非第1个下拉选项
                        int rowCount = sheet2.getLastRowNum();
                        // System.out.println("========== LastRowNum =========" + rowCount);
                        if (j <= rowCount) { // 前面创建过的行，直接获取行，创建列
                            // 获取行，创建列
                            sheet2.getRow(j).createCell(index).setCellValue(dlData[j]); // 设置对应单元格的值
                        } else { // 未创建过的行，直接创建行、创建列
                            sheet2.setColumnWidth(j, 4000); // 设置每列的列宽
                            // 创建行、创建列
                            sheet2.createRow(j).createCell(index).setCellValue(dlData[j]); // 设置对应单元格的值
                        }
                    }
                }
                index++;
            }
        }
        File f = new File(filePath); // 写文件
        // 不存在则新增
        if (!f.getParentFile().exists() && !f.getParentFile().mkdirs()) {
            throw new RuntimeException("");
        }
        if (!f.exists() && !f.createNewFile()) {
            throw new RuntimeException("");
        }
        out = new FileOutputStream(f);
        out.flush();
        wb.write(out);
        return out;
    }

    /**
     * 下拉列表元素很多的情况 (255以上的下拉)
     * @param @param strFormula
     * @param @param firstRow   起始行
     * @param @param endRow     终止行
     * @param @param firstCol   起始列
     * @param @param endCol     终止列
     * @param @return
     * @return HSSFDataValidation
     * @throws
     */
    private static HSSFDataValidation setDataValidation(String strFormula, int firstRow, int endRow, int firstCol,
        int endCol) {
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);

        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);

        return dataValidation;
    }

    /**
     * 下拉列表元素不多的情况(255以内的下拉)
     * @param @param sheet
     * @param @param textList
     * @param @param firstRow
     * @param @param endRow
     * @param @param firstCol
     * @param @param endCol
     * @param @return
     * @return DataValidation
     * @throws
     */
    private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow,
        int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        // DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList((short)firstRow, (short)endRow, (short)firstCol,
            (short)endCol);
        // 数据有效性对象
        return helper.createValidation(constraint, regions);
    }

    /**
     * 导出数据
     * @param os
     * @param data
     * @throws IOException
     */
    public static void exportToExcel(OutputStream os, List<List<String>> data) throws IOException {
        exportToExcel(os, data, null);
    }

    /**
     * 导出数据
     * @param os
     * @param data
     * @throws IOException
     */
    public static void exportToExcel(OutputStream os, List<List<String>> data, String title) throws IOException {
        exportToExcel(os, data, title, null);
    }

    /**
     * 导出数据
     * @param os
     * @param data
     * @throws IOException
     */
    public static void exportToExcel(OutputStream os, List<List<String>> data, String title, WriteDone writeDone)
            throws IOException {
        try (HSSFWorkbook wb = new HSSFWorkbook()) {
            HSSFSheet sheet = wb.createSheet("Data");
            Map<String, CellStyle> styles = createStyles(wb);
            if (org.apache.commons.lang3.StringUtils.isNotBlank(title)) {
                Row titleRow = sheet.createRow(0);
                titleRow.setHeightInPoints(30);
                Cell titleCell = titleRow.createCell(0);
                titleCell.setCellStyle(styles.get("title"));
                titleCell.setCellValue(title);
                sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(),
                    titleRow.getRowNum(), data.get(0).size() - 1));
            }
            int col = 0;
            for (int r = 0; r < data.size(); r++) {
                HSSFRow row = sheet.createRow(r + 1);
                List<String> cols = data.get(r);
                for (int c = 0; c < cols.size(); c++) {
                    HSSFCell cell = row.createCell(c);
                    if (r == 0) {
                        cell.setCellStyle(styles.get("header"));
                    } else {
                        cell.setCellStyle(styles.get("data"));
                    }
                    cell.setCellValue(new HSSFRichTextString(cols.get(c)));
                }
                col = Math.max(col, cols.size());
            }
            for (int i = 0; i < col; i++) {
                sheet.autoSizeColumn((short)i); // 调整宽度
            }
            if (writeDone != null) {
                writeDone.invoke(sheet);
            }
            if (wb != null) {
                wb.write(os);
            }
        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            os.flush();
            os.close();
        }
    }

    private static Map<String, CellStyle> createStyles(HSSFWorkbook wb) {
        Map<String, CellStyle> styleMap = Maps.newHashMap();
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short)16);
        titleFont.setBold(true);
        style.setFont(titleFont);
        styleMap.put("title", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short)10);
        style.setFont(dataFont);
        styleMap.put("data", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styleMap.get("data"));
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short)10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(headerFont);
        styleMap.put("header", style);
        return styleMap;
    }
}
