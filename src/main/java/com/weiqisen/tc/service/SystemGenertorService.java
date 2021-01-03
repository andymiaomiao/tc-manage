package com.weiqisen.tc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemGenertor;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;


/**
 * 操作资源管理
 *
 * @author liuyadu
 */
public interface SystemGenertorService extends IBaseService<SystemGenertor> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemGenertor> findPage(PageParams pageParams);

    /**
     * 检查操作编码是否存在
     *
     * @param actionCode
     * @return
     */
    Boolean isExist(String actionCode,String port,String schemas);


    /**
     * 添加操作资源
     *
     * @param action
     * @return
     */
    SystemGenertor add(SystemGenertor action);

    /**
     * 修改操作资源
     *
     * @param action
     * @return
     */
    SystemGenertor update(SystemGenertor action);

    /**
     * 移除操作
     *
     * @param actionId
     * @return
     */
    void remove(Long actionId);

}
