package com.weiqisen.tc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemDict;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;


import java.util.List;

/**
 * 接口资源管理
 *
 * @author liuyadu
 */
public interface SystemDictService extends IBaseService<SystemDict> {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemDict> findPage(PageParams pageParams);

    /**
     * 获取服务列表
     *
     * @return
     */
    List<SystemDict> getServiceList(SystemDict query);

    /**
     * 添加角色
     *
     * @param systemServer 角色
     * @return
     */
    SystemDict add(SystemDict systemServer);

    /**
     * 更新角色
     *
     * @param systemServer 角色
     * @return
     */
    SystemDict update(SystemDict systemServer);

    /**
     * 删除角色
     *
     * @param serverId 角色ID
     * @return
     */
    void remove(Long serverId);

    /**
     * 检测角色编码是否存在
     *
     * @param serverName
     * @return
     */
    Boolean isExist(String serverName);
}
