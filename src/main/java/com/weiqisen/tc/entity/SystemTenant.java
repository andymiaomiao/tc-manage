package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统角色-基础信息
 *
 * @author: weiqisen
 * @date: 2018/10/24 16:21
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("system_tenant")
public class SystemTenant extends AbstractEntity {
    private static final long serialVersionUID = 5197785628543375591L;
    /**
     * 租户ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long tenantId;

    /**
     * 租户名称
     */
    private String tenantName;

    /**
     * 租户描述
     */
    private String tenantDesc;

    /**
     * 租户类型1 平台，10 设备平台，20 服务商，30 采购商，40 仓库，50
     */
    private Integer tenantType;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 联系地址
     */
    private String address;
}
