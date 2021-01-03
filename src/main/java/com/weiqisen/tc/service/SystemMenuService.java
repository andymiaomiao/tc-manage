package com.weiqisen.tc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemMenu;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 菜单资源管理
 *
 * @author weiqisen
 */
public interface SystemMenuService extends IBaseService<SystemMenu> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemMenu> findPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<SystemMenu> findList(PageParams pageParams);

    /**
     * 检查菜单编码是否存在
     *
     * @param menuCode
     * @return
     */
    Boolean isExist(String menuCode, Integer menuType);


    /**
     * 添加菜单资源
     *
     * @param menu
     * @return
     */
    SystemMenu add(SystemMenu menu);

    /**
     * 修改菜单资源
     *
     * @param menu
     * @return
     */
    SystemMenu update(SystemMenu menu);

    /**
     * 移除菜单
     *
     * @param menuId
     * @return
     */
    void remove(Long menuId);

    List<SystemMenu> findMenuByUser(Map<String, Object> paramMap);

    List<SystemMenu> findCustMenuByUser(Map<String, Object> paramMap);

    void addMenuApi(Long menuId, String... apiIds);

    void removeAuthorityMenu(Long actionId);


}
