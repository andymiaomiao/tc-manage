package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.weiqisen.tc.annotation.TableAlias;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统-设备信息
 *
 * @author weiqisen
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableAlias("device")
@TableName("tc_device")
public class TcDevice extends AbstractEntity {
    private static final long serialVersionUID = -735161640894047414L;

    @TableId(type = IdType.ID_WORKER)
    private Long deviceId;

    private Long categoryId;

    private String deviceName;

    private String deviceSn;

    private String deviceRom;

    private String model;

    private String appearance;

    private String detail;

    private int status;

    private int cost;

    private int sell;

    private int profit;

    private String lists;

    private String battery;

    private String systemVersion;

}
