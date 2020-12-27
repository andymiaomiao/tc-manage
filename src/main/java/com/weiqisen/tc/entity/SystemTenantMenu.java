package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统角色-角色与用户关联
 *
 * @author: weiqisen
 * @date: 2018/10/24 16:21
 * @description:
 */
@Data
@EqualsAndHashCode()
@NoArgsConstructor
@TableName("system_tenant_menu")
public class SystemTenantMenu {
    private static final long serialVersionUID = -667816444278087761L;
    /**
     * 菜单ID
     */
    @TableId(type= IdType.INPUT)
    private Long menuId;

    /**
     * 租户ID
     */
    private Long tenantId;
}
