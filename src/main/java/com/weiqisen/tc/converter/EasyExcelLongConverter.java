package com.weiqisen.tc.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-08-28 12:39
 */
public class EasyExcelLongConverter implements Converter<Long> {
    @Override
    public Class supportJavaTypeKey() {
        return null;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public Long convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        return null;
    }

    @Override
    public CellData convertToExcelData(Long aLong, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        if(aLong !=null){
            return new CellData(String.valueOf(aLong));
        }else {
            return new CellData("");
        }
    }
}
