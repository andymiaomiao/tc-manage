package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.DiskFile;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.form.req.DeviceBatchRes;
import com.opencloud.device.form.req.DeviceInfoRes;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.mybatis.base.service.IBaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 系统用户登录账号管理
 * 支持多账号登陆
 *
 * @author liuyadu
 */
public interface IotDeviceService extends IBaseService<IotDevice> {


    IPage<IotDevice> findPage(PageParams pageParams);

    /**
     * 查询设备列表
     *
     * @return
     */
    List<IotDevice> findList(RequestParams requestParams);

    /**
     * 添加菜单资源
     *
     * @param iotDevice
     * @return
     */
    IotDevice add(IotDevice iotDevice);

    /**
     * 修改菜单资源
     *
     * @param iotDevice
     * @return
     */
    IotDevice update(IotDevice iotDevice);

    /**
     * 修改菜单资源
     *
     * @param iotDevice
     * @return
     */
    IotDevice adjustUpdate(IotDevice iotDevice);

    /**
     * 移除操作
     *
     * @param deviceId
     * @return
     */
    void remove(Long deviceId);

    /**
     * 批量移除操作
     *
     * @param requestParams
     * @return
     */
    void batchRemove(RequestParams requestParams);

    void getDeviceInfoToCloud(DeviceInfoRes deviceInfoRes);

    void getDeviceSoftToCloud(DeviceInfoRes deviceInfoRes);

    Map<String,Object> getDeviceInfoDashboard();

    Map<String,Object> getDeviceList();
}
