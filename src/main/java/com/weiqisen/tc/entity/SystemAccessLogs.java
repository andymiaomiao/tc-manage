package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-08-10 15:04
 */
@Data
@NoArgsConstructor
@TableName("system_access_logs")
public class SystemAccessLogs extends AbstractEntity {
    @TableId(type = IdType.ID_WORKER)
    private Long accessId;
    private Long tenantId;
    private Long userId;
    private String userName;
    private String path;
    private String params;
    private String result;
    private String headers;
    private String ip;
    private String httpStatus;
    private String method;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "request_time", fill = FieldFill.INSERT_UPDATE)
    private String requestTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "response_time", fill = FieldFill.INSERT_UPDATE)
    private String responseTime;
    private String useTime;
    private String userAgent;
    private String region;
    private String authentication;
    private String serverName;
    private String optionName;
    private String error;

}
