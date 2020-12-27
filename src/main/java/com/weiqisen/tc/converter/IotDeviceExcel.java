package com.weiqisen.tc.converter;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 系统用户-登录账号
 *
 * @author weiqisen
 */
@Data
@ExcelIgnoreUnannotated
public class IotDeviceExcel {
    private static final long serialVersionUID = 1L;
    @ExcelProperty(value = "设备名称", index = 0)
    private String deviceName;
    @ExcelProperty(value = "设备编号", index = 1)
    private String deviceSn;
    @ExcelProperty(value = "设备分布地ID", index = 2, converter = EasyExcelLongConverter.class)
    private Long distribuId;
    @ExcelProperty(value = "设备分布地", index = 3)
    private String distribuName;
    @ExcelProperty(value = "设备分类ID", index = 4, converter = EasyExcelLongConverter.class)
    private Long categoryId;
    @ExcelProperty(value = "设备分类", index = 5)
    private String categoryName;

    @ExcelProperty(value = "设备分布式标注x轴", index = 6)
    private BigDecimal distribuImgX;
    @ExcelProperty(value = "设备分布式标注y轴", index = 7)
    private BigDecimal distribuImgY;

    private String name;
}
