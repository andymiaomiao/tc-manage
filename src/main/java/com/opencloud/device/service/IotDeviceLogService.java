package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.IotDeviceLog;
import com.opencloud.device.form.req.DeviceLofInfoRes;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.service.IBaseService;

import java.util.Map;

/**
 * 角色管理
 *
 * @author liuyadu
 */
public interface IotDeviceLogService extends IBaseService<IotDeviceLog> {

    /**
     * 获取设备日志分页列表
     *
     * @param pageParams
     * @return
     */
    IPage<Map<String, Object>> findPage(PageParams pageParams);

    void getDeviceLogToCloud(DeviceLofInfoRes deviceLofInfoRes);

}
