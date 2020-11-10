package com.opencloud.device.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.opencloud.device.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 系统资源-菜单信息
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("iot_device_distribution")
public class IotDeviceDistribution extends AbstractEntity {
    private static final long serialVersionUID = -4414780909980518788L;
    /**
     * Id
     */
    @TableId(type= IdType.ID_WORKER)
    private Long distributionId;
    private Long tenantId;
    private String distributionName;
    private BigDecimal lat;
    private BigDecimal lon;
    private String imgPath;

}
