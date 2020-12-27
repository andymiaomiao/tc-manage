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
@TableName("system_role")
public class SystemRole extends AbstractEntity {
    private static final long serialVersionUID = 5197785628543375591L;
    /**
     * 角色ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long roleId;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 类型
     */
    private Integer roleType;

    /**
     * 状态:0-无效 1-有效
     */
    private Integer status;
    /**
     * 是否系统0-否 1-是 禁止删除
     */
    private Integer isSystem;
    /**
     * 默认数据0-否 1-是 禁止删除
     */
    private Integer isPersist;
}
