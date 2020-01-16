package com.lwx.utils.controller.uploadexcel;

import com.lwx.utils.dto.TypeDto;
import com.lwx.utils.service.TypeService;
import com.lwx.utils.utillist.ExportExcel;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luwenxin
 * @create 2020/1/13
 */
@Slf4j
@RestController
@RequestMapping("/upload/importExcel")
public class ImportExcelController {

    @Autowired
    private TypeService typeService;

    public String importTest() {
        log.info("您已经入");
        log.error("--error---");
        return "<input value='你好' disable='disable'>";
    }

    /**
     * @author: LWX
     * @date: 2020/1/15 14:13
     * xlsx2017
     */
    @RequestMapping("/import")
    public static void importIoFile() throws IOException {
        log.info("=============开始读取excel===========");
        File file = new File("D:\\Downloads\\2020.xlsx");
        System.out.println("file==" + file);
        //获得该文件的输入流
        FileInputStream stream = new FileInputStream(file);
        // 多态  抛异常
        Workbook sheets = new XSSFWorkbook(stream);
        //获取一个工作表(sheet页)，下标从0开始
        Sheet sheet = sheets.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {

            // 获取行数
            Row row = sheet.getRow(i);
            // 获取单元格 取值
            String value1 = row.getCell(0).getStringCellValue();
            String value2 = row.getCell(1).getStringCellValue();
            String value3 = row.getCell(2).getStringCellValue();
            String value4 = row.getCell(3).getStringCellValue();
            String value5 = row.getCell(4).getStringCellValue();

            System.out.print(value1 + "  ");
            System.out.print(value2 + "  ");
            System.out.print(value3 + "  ");
            System.out.print(value4 + "  ");
            System.out.print(value5);
        }


        //关流
        sheets.close();
        stream.close();
        log.info("===读取完毕====");
    }

    /**
     * @author: LWX
     * @date: 2020/1/15 14:13
     * 内容Excel模板下载
     */
    @GetMapping("/getExcel")
    public ResponseEntity<byte[]> templateDownload() throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode("内容导入模板.xls", "UTF-8"));
        ClassPathResource cpr = new ClassPathResource("excel/内容导入模板.xls");
        byte[] bytes = FileCopyUtils.copyToByteArray(cpr.getInputStream());
        ResponseEntity<byte[]> response = new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
        return response;
    }

    /**
     * @author: LWX
     * @date: 2020/1/15 14:13
     * xls2013
     */
    @PostMapping("/upload")
    public String importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "编号");
        map.put("type", "类型");
        map.put("carNo", "车牌");
        List<TypeDto> list = ExportExcel.toList(file.getInputStream(), TypeDto.class, map);
        list.forEach(content -> {
            Map<String, String> result = typeService.add(content);
            log.info("result===" + result.toString());
//            Optional.ofNullable(content).ifPresent(result->);
        });
        log.info("==========content=======" + list.toString());
        return "上传成功";
    }
}
