package com.demo.weekreport;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 基础数据类
 *
 * @author Jiaju Zhuang
 **/
@Data
public class DownloadData {
    @ExcelProperty("姓名")
    private String string;
    @ExcelProperty("工作描述")
    private String content;
    @ExcelProperty("日期")
    private Date date;
    @ExcelProperty("总结")
    private Double doubleData;
}
