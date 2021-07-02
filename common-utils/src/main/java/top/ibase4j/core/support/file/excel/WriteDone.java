package top.ibase4j.core.support.file.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author ShenHuaJie
 * @since 2019年4月21日 下午3:21:19
 */
public interface WriteDone {
    void invoke(HSSFSheet sheet);
}
