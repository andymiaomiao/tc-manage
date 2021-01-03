package com.weiqisen.tc.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemClient;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;

/**
 * 操作资源管理
 *
 * @author liuyadu
 */
public interface SystemClientService extends IBaseService<SystemClient> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemClient> findPage(PageParams pageParams);
//
//    /**
//     * 查询菜单下所有操作
//     *
//     * @param menuId
//     * @return
//     */
//    List<SystemClient> findListByMenuId(Long menuId);
//
//    /**
//     * 检查操作编码是否存在
//     *
//     * @param actionCode
//     * @return
//     */
//    Boolean isExist(String actionCode);


    /**
     * 添加操作资源
     *
     * @param action
     * @return
     */
    SystemClient add(SystemClient action);

    /**
     * 根据appId查找对应商户类型
     * @param appId
     * @return
     */
    SystemClient find(String appId);

    /**
     * 修改操作资源
     *
     * @param action
     * @return
     */
    SystemClient update(SystemClient action);

    /**
     * 移除操作
     *
     * @param actionId
     * @return
     */
    void remove(Long actionId);

    void addClientApi(Long clientId, String... apiIds);

//    /**
//     * 移除菜单相关资源
//     *
//     * @param menuId
//     */
//    void removeByMenuId(Long menuId);
//
//    List<BaseAuthority> findActionByUser(Long userId, Long tenantId, boolean equals, String type);
//


    void removeAuthorityClient(Long actionId);
//
//    List<BaseAuthority> findAppActionByUser(Long userId, String tenantType, String userType);
}
