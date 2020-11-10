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

//    void getAlarmRepairToDevice(Long deviceId);
//
//    /**
//     * 获取app信息
//     *
//     * @param appId
//     * @return
//     */
//    IotDeviceAlarm get(String appId);
//
//    /**
//     * 根据ApiKey获取app信息
//     * @param apiKey
//     * @return
//     */
//    IotDeviceAlarm getByApiKey(String apiKey);
//
//    /**
//     * 获取app和应用信息
//     *
//     * @param clientId
//     * @return
//     */
//    BaseClientInfo getByClientId(String clientId);
//
//
//    /**
//     * 更新应用开发新型
//     *
//     * @param client
//     */
//    void updateClientInfo(BaseClientInfo client);
//
//    /**
//     * 添加应用
//     *
//     * @param systemApp 应用
//     * @return 应用信息
//     */
//    IotDeviceAlarm add(IotDeviceAlarm systemApp);
//
//    /**
//     * 修改应用
//     *
//     * @param systemApp 应用
//     * @return 应用信息
//     */
//    IotDeviceAlarm update(IotDeviceAlarm systemApp);
//
//
//    /**
//     * 重置秘钥
//     *
//     * @param appId
//     * @return
//     */
//    String restSecret(String appId);
//
//    /**
//     * 删除应用
//     *
//     * @param appId
//     * @return
//     */
//    void remove(String appId);
}
