package com.opencloud.device.schedule;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.entity.SystemUser;
import com.opencloud.device.model.*;
import com.opencloud.device.mqtt.MqttMessageSender;
import com.opencloud.device.service.IotDeviceService;
import com.opencloud.device.service.SystemUserService;
import com.opencloud.device.utils.DateUtils;
import com.opencloud.device.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author jihao
 * @version 1.0
 * 1.主要用于标记配置类，兼备Component的效果。
 * 2.开启定时任务
 * @date 2020-08-19 15:24
 */

@Configuration
@EnableScheduling
public class SaticScheduleTask {

    @Autowired
    private IotDeviceService iotDeviceService;

    @Autowired
    private MqttMessageSender mqttMessageSender;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private SystemUserService systemUserService;
    /**
     * 添加定时任务
     * 或直接指定时间间隔，例如：10秒
     * @Scheduled(fixedRate=10000)
     */
    @Scheduled(cron = "0/10 * * * * ?")
    private void configureTasks() {
        List<IotDevice> list = iotDeviceService.findList(new RequestParams(new HashMap(16)));
        if(list!=null&&list.size()>0) {
            list.stream().forEach(item->{
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", IdWorker.getIdStr());
                jsonObject.put("deviceid", item.getDeviceSn());
                jsonObject.put("msgcode", DeviceConstants.DEVICE_CHANNL_1001);
                mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_INFO + item.getDeviceSn(), jsonObject.toJSONString());
            });
        }
        System.err.println("执行定时任务时间: " + LocalDateTime.now());
    }



    /**
     * 添加定时任务
     * 每天检查一次用户状态
     * @Scheduled(fixedRate=10000)
     */
//    @Scheduled(cron = "0 0 2 * * ?")
    @Scheduled(cron = "*/20 * * * * ?")
    private void configureLocakUserTasks() {
        Map<String, Object> map = new HashMap<>(16);
        map.put("pageNO",1);
        map.put("pageSize",1000);
        IPage<SystemUser> page = systemUserService.findUserPage(new PageParams(map));
        if(page.getRecords()!=null){
            List<SystemUser> records = page.getRecords();
            if(records!=null&&records.size()>0){
                SystemUser stringObjectMap = records.get(records.size()-1);
                Date createTime = stringObjectMap.getCreateTime();
                Date addDate = DateUtils.getAddDate(createTime, Calendar.DATE, 1);
                stringObjectMap.setUpdateTime(addDate);
                long between = DateUtil.between(createTime, addDate, DateUnit.DAY);
                System.out.println("差值日期："+between);
                if(between>=30){
                    systemUserService.lockUserAll();
                }else{
                    systemUserService.lockUserUpdateAll(stringObjectMap);
                }
            }
        }
        System.out.println("执行定时任务时间: " + LocalDateTime.now());
    }


    /**
     * 添加定时任务
     * 或直接指定时间间隔，例如：5分钟
     * @Scheduled(fixedRate=10000)
     */
    @Scheduled(cron = "0 */1 * * * ?")
    private void configureScanDeviceTasks() throws InterruptedException {
        List<IotDevice> IotDeviceList = iotDeviceService.findList(new RequestParams(new HashMap(16)));
        if(IotDeviceList!=null&&IotDeviceList.size()>0) {
            for (int i=0;i< IotDeviceList.size();i++) {
                String deviceSn = IotDeviceList.get(i).getDeviceSn();
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", IdWorker.getIdStr());
                jsonObj.put("msgcode", DeviceConstants.DEVICE_CHANNL_1001);
                Object deviceSend = redisUtil.get(DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + jsonObj.getString("deviceSn"));
                if (deviceSend != null) {
                    int deviceSendNum = (int) deviceSend;
                    if (deviceSendNum < 3) {
                        System.out.println("发过消息数量少于3次的状态：" + DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + deviceSn);
                        redisUtil.set(DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + deviceSn, deviceSendNum + 1, 3, TimeUnit.MINUTES);
                        mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_INFO + deviceSn, jsonObj.toJSONString());
                    } else if (deviceSendNum >= 3 && deviceSendNum <10) {
                        System.out.println("发过消息数量大于或等于3次的状态：" + DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + deviceSn);
                        IotDevice device = new IotDevice();
                        device.setDeviceId(IotDeviceList.get(i).getDeviceId());
                        device.setOnline(0);
                        jsonObj.put("online", 0);
                        if(IotDeviceList.get(i).getOnline().equals(1)) {
                            iotDeviceService.save(device);
                        }
                        redisUtil.set(DeviceRedisKey.DEVICE_REDIS_STATUS +deviceSn,0,5,TimeUnit.MINUTES);
                        redisUtil.del(DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + deviceSn);
                    }
                } else {
                    System.out.println("测试测试测试测试没有发过消息是的状态：" + DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + jsonObj.getString("deviceSn"));
                    redisUtil.set(DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + jsonObj.getString("deviceSn"), 1, 3, TimeUnit.MINUTES);
                    mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_INFO + deviceSn, jsonObj.toJSONString());
                }
                Thread.sleep(200);
            }
        }
        System.out.println("执行定时任务时间: " + LocalDateTime.now());
    }
}
