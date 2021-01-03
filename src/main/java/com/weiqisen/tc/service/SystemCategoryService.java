package com.weiqisen.tc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemCategory;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.RequestParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;


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
    Boolean isExist(String name,Long tenantId);

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
