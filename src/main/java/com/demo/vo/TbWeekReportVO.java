package com.demo.vo;


import com.alibaba.excel.annotation.ExcelProperty;
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
    @ColumnWidth(50)
    @ExcelProperty("总结")
    private String summary;
    @ColumnWidth(50)
    @ExcelProperty("工作描述")
    private String content;
    @ExcelProperty("日期")
    @ColumnWidth(20)
    private Date reportDate;
}
