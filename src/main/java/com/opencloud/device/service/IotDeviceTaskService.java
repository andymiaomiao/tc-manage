package com.opencloud.device.service;


import com.opencloud.device.entity.IotDeviceTask;
import com.opencloud.device.mybatis.base.service.IBaseService;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 17:18
 */
public interface IotDeviceTaskService extends IBaseService<IotDeviceTask> {
    /**
     * 添加菜单资源
     *
     * @param iotDeviceTask
     * @return
     */
    IotDeviceTask add(IotDeviceTask iotDeviceTask);

    /**
     * 修改菜单资源
     *
     * @param iotDeviceTask
     * @return
     */
    IotDeviceTask update(IotDeviceTask iotDeviceTask);


}
