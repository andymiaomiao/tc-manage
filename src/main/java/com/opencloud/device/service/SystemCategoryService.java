package com.opencloud.device.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.SystemCategory;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * 操作资源管理
 *
 * @author liuyadu
 */
public interface SystemCategoryService extends IBaseService<SystemCategory> {


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemCategory> findPage(PageParams pageParams);

    /**
     * 分页查询
     *
     * @param requestParams
     * @return
     */
    List<SystemCategory> findList(RequestParams requestParams);

    /**
     * 检查分类是否存在
     *
     * @param name
     * @return
     */
    Boolean isExist(String name, Long tenantId);

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    void add(SystemCategory category);

    /**
     * 修改分类
     *
     * @param category
     * @return
     */
    void update(SystemCategory category);

    /**
     * 移除操作
     *
     * @param categoryId
     * @return
     */
    void remove(Long categoryId);
}
