package com.opencloud.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.device.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 17:15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName(value="iot_device_task")
public class IotDeviceTask extends AbstractEntity {
    private static final long serialVersionUID = -4414780909980518788L;
    /**
     * ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long taskId;
    private Long deviceId;
    private String deviceSn;
    private String code;
    private Integer status;
    private String content;

}
