package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.IotDeviceAlarm;
import com.opencloud.device.form.req.AlarmInfoRes;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.service.IBaseService;

/**
 * 应用信息管理
 *
 * @author liuyadu
 */
public interface IotDeviceAlarmService extends IBaseService<IotDeviceAlarm> {

    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    IPage<IotDeviceAlarm> findPage(PageParams pageParams);

    void getDeviceAlarmToCloud(AlarmInfoRes alarmInfoRes);


}
