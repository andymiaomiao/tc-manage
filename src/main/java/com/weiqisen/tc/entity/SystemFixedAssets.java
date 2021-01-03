package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;

import java.util.Date;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-10 15:04
 */
@Data
@NoArgsConstructor
@TableName("system_fixed_assets")
public class SystemFixedAssets extends AbstractEntity {
    @TableId(type = IdType.ID_WORKER)
    private Long fixedId;
    private Long tenantId;
    private String fixedCode;
    private String billCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "use_time", fill = FieldFill.INSERT)
    private Date useTime;
    private String fixedName;
    private String fixedDept;
    private String fixedType;
    private String address;
    private String user;
    private Integer status;
    private Integer sort;
}



