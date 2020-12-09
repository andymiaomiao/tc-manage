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

}
