package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.SystemTenant;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author liuyadu
 */
public interface SystemTenantService extends IBaseService<SystemTenant> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemTenant> findPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<SystemTenant> findList();

    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    SystemTenant add(SystemTenant role);

    /**
     * 更新角色
     *
     * @param role 角色
     * @return
     */
    SystemTenant update(SystemTenant role);

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    void remove(Long roleId);

    /**
     * 检测角色编码是否存在
     *
     * @param roleCode
     * @return
     */
    Boolean isExist(String roleCode);

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     */
    void saveUsers(Long roleId, String... userIds);

    Map<String, Object> authorityTenantMenu(Integer tenantType, Long tenantId);

    void grantTenantMenu(Long tenantId, String... menuIds);

}
