package com.opencloud.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.device.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 系统用户-登录账号
 *
 * @author liuyadu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("iot_device")
public class IotDevice extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.ID_WORKER)
    private Long deviceId;
    private Long tenantId;
    @TableField(exist = false)
    private String distribuName;
    private Long distribuId;
    private Long categoryId;
    private String categoryName;
    private String deviceName;
    private String deviceIp;
    private String deviceNew;
    private String deviceBug;
    private String deviceRom;
    private String deviceSn;
    private String protocol;
    private Integer online;
    private Integer secrecyLevel;
    private String tags;
    private Double lat;
    private Double lon;
    private String softwareVersion;
    private Double distribuImgX;
    private Double distribuImgY;
    private Double distribuImgW;
    private Double distribuImgH;
    private Double upgradeProgress;
    private Integer upgradeStatus;
    private Double uploadProgress;
    private Integer uploadStatus;
    private Integer ethernet;
    private Integer modem;
    private Integer ram;
    private Integer data;
    private Integer simCard;
}
