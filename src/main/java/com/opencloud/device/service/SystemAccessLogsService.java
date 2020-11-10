package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.SystemAccessLogs;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.service.IBaseService;

/**
 * 操作资源管理
 *
 * @author liuyadu
 */
public interface SystemAccessLogsService extends IBaseService<SystemAccessLogs> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemAccessLogs> findPage(PageParams pageParams);
//
//    /**
//     * 查询菜单下所有操作
//     *
//     * @param menuId
//     * @return
//     */
//    List<SystemAction> findListByMenuId(Long menuId);
//
//    /**
//     * 检查操作编码是否存在
//     *
//     * @param actionCode
//     * @return
//     */
//    Boolean isExist(String actionCode);
//
//
//    /**
//     * 添加操作资源
//     *
//     * @param action
//     * @return
//     */
//    SystemAction add(SystemAction action);
//
//    /**
//     * 修改操作资源
//     *
//     * @param action
//     * @return
//     */
//    SystemAction update(SystemAction action);
//
//    /**
//     * 移除操作
//     *
//     * @param actionId
//     * @return
//     */
//    void remove(Long actionId);
//
//    /**
//     * 移除菜单相关资源
//     *
//     * @param menuId
//     */
//    void removeByMenuId(Long menuId);
//
//    List<BaseAuthority> findActionByUser(Long userId, Long tenantId, boolean equals, String type);
//
//    void addActionApi(Long actionId, String... apiIds);
//
//    void removeAuthorityAction(Long actionId);
}
