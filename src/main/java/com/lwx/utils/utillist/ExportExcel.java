package com.lwx.utils.utillist;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author luwenxin
 * @create 2020/1/14
 */
@Slf4j
public class ExportExcel {
    public static <T> List<T> toList(InputStream file, Class<T> clazz, Map<String, String> map) {
        List list = new ArrayList();

        HSSFWorkbook wb = null;
        try {
            InputStream is = file;
            POIFSFileSystem fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
//            throw new BusinessException("文件错误!", e);
            log.error("文件错误!", e);
        }

        HSSFSheet sheet0 = wb.getSheetAt(0);
        List monthList = new ArrayList();
        List typeList = new ArrayList();
        HSSFRow row = sheet0.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        for (int i = 0; i < colNum; i++) {
            HSSFCell cell = row.getCell(i);
            if (1 == cell.getCellType()) {
                String month = cell.getRichStringCellValue().toString();
                month = getKeyByValue(map, month);
                monthList.add(month);
                try {
                    String type = clazz.getDeclaredField(month).getType().getName();
                    typeList.add(type);
                } catch (NoSuchFieldException e) {
//                    throw new BusinessException("类型错误,未发现含表头的类型!", e);
                    log.error("类型错误,未发现含表头的类型!", e);
                }
            } else {
//                throw new BusinessException("类型错误,请将表头内容转换为字符串类型!");
                log.error("类型错误,请将表头内容转换为字符串类型!");
            }
        }
        int rowNum = sheet0.getLastRowNum() + 1;
        for (int i = 1; i < rowNum; i++) {
            row = sheet0.getRow(i);
            if (row == null) {
                break;
            }
            Object demo = null;
            try {
                demo = clazz.newInstance();
            } catch (InstantiationException e) {
//                throw new BusinessException("实例化异常",e);
                log.error("实例化异常", e);
            } catch (IllegalAccessException e) {
//                throw new BusinessException("非法存取异常",e);
                log.error("非法存取异常", e);
            }
            for (int j = 0; j < colNum; j++) {
                HSSFCell cell = row.getCell(j);
                String type = (String) typeList.get(j);
                Object text = new Object();
                try {
                    text = getStringCellValue(cell, type);
                } catch (Exception e) {
//                    throw new BusinessException("单元格输入格式错误，请按照模板样式进行输入操作!");
                    log.error("单元格输入格式错误，请按照模板样式进行输入操作!");
                }
                if (text != null) {
                    String key = (String) monthList.get(j);
                    Field field = ReflectionUtils.findField(demo.getClass(), key);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, demo, text);
                }
            }
            list.add(demo);
        }
        return list;
    }

    private static Object getStringCellValue(HSSFCell cell, String type) {
        Object strCell = null;
        if (cell == null) {
            return strCell;
        }
        if ("java.lang.Integer".equals(type)) {
            strCell = cell.getNumericCellValue();
        } else if ("java.lang.String".equals(type)) {
            strCell = cell.getStringCellValue();
        } else if ("java.lang.Boolean".equals(type)) {
            strCell = cell.getBooleanCellValue();
        } else if ("java.util.Date".equals(type)) {
            strCell = cell.getDateCellValue();
        } else if ("java.math.BigDecimal".equals(type)) {
            strCell = new BigDecimal(cell.getNumericCellValue());
        } else {
            strCell = null;
        }
        return strCell;
    }

    private static String getKeyByValue(Map map, Object value) {
        String keys = "";
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object obj = entry.getValue();
            if (obj != null && obj.equals(value)) {
                keys = (String) entry.getKey();
            }
        }
        return keys;
    }
}
