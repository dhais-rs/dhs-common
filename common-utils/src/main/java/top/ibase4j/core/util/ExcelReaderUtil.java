package top.ibase4j.core.util;

import top.ibase4j.core.support.file.excel.Excel2003Reader;
import top.ibase4j.core.support.file.excel.Excel2007Reader;
import top.ibase4j.core.support.file.excel.RowReader;
import org.apache.commons.fileupload.FileItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ShenHuaJie
 * @since 2019年4月4日 下午3:01:36
 */
public final class ExcelReaderUtil {
    private ExcelReaderUtil() {
    }

    private static final Logger logger = LogManager.getLogger();

    // excel2003扩展名
    public static final String EXCEL03_EXTENSION = ".xls";
    // excel2007扩展名
    public static final String EXCEL07_EXTENSION = ".xlsx";

    /**
     * 读取Excel文件，可能是03也可能是07版本
     *
     * @param reader
     * @param fileName
     * @throws Exception
     */
    public static void readExcel(String fileName, RowReader reader) throws Exception {
        // 处理excel2003文件
        if (fileName.endsWith(EXCEL03_EXTENSION)) {
            Excel2003Reader excel03 = new Excel2003Reader(reader);
            excel03.process(fileName);
            // 处理excel2007文件
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {
            Excel2007Reader excel07 = new Excel2007Reader(reader);
            excel07.process(fileName);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
    }

    /**
     * 读取Excel文件，可能是03也可能是07版本
     *
     * @param reader
     * @param fileName
     * @param inputStream
     * @throws Exception
     */
    public static void readExcel(String fileName, InputStream inputStream, RowReader reader) throws Exception {
        // 处理excel2003文件
        if (fileName.endsWith(EXCEL03_EXTENSION)) {
            Excel2003Reader excel03 = new Excel2003Reader(reader);
            excel03.process(inputStream);
            // 处理excel2007文件
        } else if (fileName.endsWith(EXCEL07_EXTENSION)) {
            Excel2007Reader excel07 = new Excel2007Reader(reader);
            excel07.process(inputStream);
        } else {
            throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
        }
    }

    /**
     * 获取Excel数据,返回List
     *
     * @param sheetNumber
     *            读取工作表的下标(从1开始).可有可无,默认读取所有表单.
     */
    public static final List<String[]> excelToArrayList(String filePath, int... sheetNumber) throws Exception {
        List<String[]> resultList = null;
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            resultList = excelToArrayList(filePath, is, sheetNumber);
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return resultList;
    }

    /**
     * 获取Excel数据,返回List
     *
     * @param sheetNumber
     *            读取工作表的下标(从1开始).可有可无,默认读取所有表单.
     */
    public static final List<String[]> excelToArrayList(String fileName, FileItem fileItem, int... sheetNumber)
            throws Exception {
        List<String[]> resultList = null;
        InputStream is = null;
        try {
            is = fileItem.getInputStream();
            resultList = excelToArrayList(fileName, is, sheetNumber);
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return resultList;
    }

    /**
     * 获取Excel数据,返回List
     *
     * @param sheetNumber
     *            读取工作表的下标(从1开始).可有可无,默认读取所有表单.
     */
    public static final List<String[]> excelToArrayList(String fileName, InputStream is, final int... sheetNumber)
            throws Exception {
        final ArrayList<String[]> resultList = new ArrayList<String[]>();
        try {
            readExcel(fileName, is, new RowReader() {
                @Override
                public void invoke(int sheetIndex, int curRow, List<String> rowlist) {
                    if (sheetNumber == null || sheetNumber.length == 0) {
                        resultList.add(rowlist.toArray(new String[]{}));
                    } else {
                        for (int element : sheetNumber) {// 循环工作表
                            if (sheetIndex == element) {
                                resultList.add(rowlist.toArray(new String[]{}));
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            logger.error("Read excel error.", e);
            throw e;
        }
        return resultList;
    }
}
