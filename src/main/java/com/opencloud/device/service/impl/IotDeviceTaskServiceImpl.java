package com.opencloud.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.opencloud.device.entity.IotDeviceTask;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.mapper.IotDeviceTaskMapper;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.IotDeviceTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 17:19
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IotDeviceTaskServiceImpl extends BaseServiceImpl<IotDeviceTaskMapper, IotDeviceTask> implements IotDeviceTaskService {

    /**
     * 添加菜单资源
     *
     * @param deviceTask
     * @return
     */
    @Override
    public IotDeviceTask add(IotDeviceTask deviceTask) {
        save(deviceTask);
        return deviceTask;
    }

    /**
     * 修改菜单资源
     *
     * @param deviceTask
     * @return
     */
    @Override
    public IotDeviceTask update(IotDeviceTask deviceTask) {
        IotDeviceTask saved = getById(deviceTask.getTaskId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在!", deviceTask.getDeviceSn()));
        }
        updateById(deviceTask);
        return deviceTask;
    }
}
