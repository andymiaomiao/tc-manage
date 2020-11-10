package com.opencloud.device.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.device.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统应用-基础信息
 *
 * @author liuyadu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("iot_device_alarm")
public class IotDeviceAlarm extends AbstractEntity {

    @TableId(type = IdType.ID_WORKER)
    private Long alarmId;
    private Long deviceId;
    private String alarmType;
    private String content;
    private Integer status;

}
