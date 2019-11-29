package com.demo.vo;


import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;

/**
 * 导出使用
 */
@Data
public class TbWeekReportVO {

    @ExcelProperty("姓名")
    private String userName;
    @ColumnWidth(80)
    @ExcelProperty("工作描述")
    private String content;
    @ColumnWidth(50)
    @ExcelProperty("总结")
    private String summary;
    @ExcelProperty("日期")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(20)
    private Date reportDate;
}
