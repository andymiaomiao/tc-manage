package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统资源-功能操作
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("system_client")
public class SystemClient extends AbstractEntity {
    private static final long serialVersionUID = 1471599074044557390L;

    /**
     * 资源ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long clientId;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 租户类型
     */
    private Integer tenantType;

    /**
     * 资源编码
     */
    private String clientCode;

    /**
     * 资源名称
     */
    private String clientName;

    /**
     * id
     */
    private String clientAppid;

    /**
     * 描述
     */
    private String clientDesc;

    /**
     * 状态
     */
    private Integer status;
}
