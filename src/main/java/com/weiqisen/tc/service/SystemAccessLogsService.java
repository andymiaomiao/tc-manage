package com.weiqisen.tc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemAccessLogs;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;

/**
 * 操作资源管理
 *
 * @author weiqisen
 */
public interface SystemAccessLogsService extends IBaseService<SystemAccessLogs> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemAccessLogs> findPage(PageParams pageParams);

}
