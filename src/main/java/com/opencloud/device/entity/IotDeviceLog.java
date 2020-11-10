package com.opencloud.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.device.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统角色-基础信息
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("iot_device_log")
public class IotDeviceLog extends AbstractEntity {
    private static final long serialVersionUID = 5197785628543375591L;
    /**
     * ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long logId;
    private Long deviceId;
    private String fileName;
    private String realName;
    private String fileUrl;
    private String fileSize;

}
