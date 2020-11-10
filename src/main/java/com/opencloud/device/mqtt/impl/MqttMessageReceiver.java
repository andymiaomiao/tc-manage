package com.opencloud.device.mqtt.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opencloud.device.form.req.AlarmInfoRes;
import com.opencloud.device.form.req.DeviceInfoRes;
import com.opencloud.device.form.req.DeviceLofInfoRes;
import com.opencloud.device.model.DeviceConstants;
import com.opencloud.device.model.MqttEnum;
import com.opencloud.device.service.IotDeviceAlarmService;
import com.opencloud.device.service.IotDeviceLogService;
import com.opencloud.device.service.IotDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * mqtt订阅消息处理
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 14:52
 */
@Slf4j
@Component
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttMessageReceiver implements MessageHandler {

    @Autowired
    private IotDeviceAlarmService iotDeviceAlarmService;

    @Autowired
    private IotDeviceService iotDeviceService;

    @Autowired
    private IotDeviceLogService iotDeviceLogService;

//    @Autowired
//    private IotDeviceAlarmServiceClient iotDeviceAlarmServiceClient;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
//        String topic = String.valueOf(message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC));
        JSONObject jsonObject = JSON.parseObject(String.valueOf(message.getPayload()));
        Long id = jsonObject.getLong("id");
        Integer status = jsonObject.getInteger("status");
        String msgcode = jsonObject.getString("msgcode");
        if(MqttEnum.DEVICE_SUC.status.equals(status)||MqttEnum.DEVICE_ACT.status.equals(status)) {
            if (DeviceConstants.DEVICE_CHANNL_1001.equals(msgcode)) {
                JSONArray msgbody = jsonObject.getJSONArray("msgbody");
                List<DeviceInfoRes> deviceInfoResList = new ArrayList<>();
                for (Object item : msgbody) {
                    JSONObject item1 = (JSONObject) item;
                    JSONObject alarmInfo = item1.getJSONObject("alarm_info");
                    //警告对象
                    AlarmInfoRes alarmInfoRes = new AlarmInfoRes();
                    alarmInfoRes.setEthernet(alarmInfo.getInteger("ethernet"));
                    alarmInfoRes.setModem(alarmInfo.getInteger("modem"));
                    alarmInfoRes.setRam(alarmInfo.getInteger("ram"));
                    alarmInfoRes.setData(alarmInfo.getInteger("data"));
                    alarmInfoRes.setSimCard(alarmInfo.getInteger("simCard"));
                    //设备对象
                    DeviceInfoRes deviceInfoRes = new DeviceInfoRes();
                    deviceInfoRes.setDeviceSn(item1.getString("deviceid"));
                    deviceInfoRes.setSoftwareVersion(item1.getString("software_version"));
                    deviceInfoRes.setOnlineStatus(item1.getInteger("online_status"));
                    deviceInfoRes.setDeviceIp(item1.getString("device_ip"));
                    deviceInfoRes.setAlarmInfo(alarmInfoRes);
                    deviceInfoResList.add(deviceInfoRes);
                }
                DeviceInfoRes deviceInfoRes1 = new DeviceInfoRes();
                deviceInfoRes1.setDeviceInfoResList(deviceInfoResList);
                deviceInfoRes1.setTaskId(id);
                deviceInfoRes1.setTaskStatus(status);
                deviceInfoRes1.setTaskCode(msgcode);
                iotDeviceService.getDeviceInfoToCloud(deviceInfoRes1);
            } else if (DeviceConstants.DEVICE_CHANNL_2001.equals(msgcode)) {
                System.out.println(jsonObject);
                String deviceid = jsonObject.getString("deviceSn");
                String uri = jsonObject.getString("uri");
                String totalSize = jsonObject.getString("totalSize");
                String completedSize = jsonObject.getString("completedSize");
                DeviceInfoRes deviceInfoRes1 = new DeviceInfoRes();
                deviceInfoRes1.setTaskId(id);
                deviceInfoRes1.setTaskStatus(status);
                deviceInfoRes1.setTaskCode(msgcode);
                deviceInfoRes1.setDeviceSn(deviceid);
                deviceInfoRes1.setUri(uri);
                deviceInfoRes1.setTotalSize(totalSize);
                deviceInfoRes1.setCompletedSize(completedSize);
                iotDeviceService.getDeviceSoftToCloud(deviceInfoRes1);
            } else if (DeviceConstants.DEVICE_CHANNL_3001.equals(msgcode)) {
                String deviceid = jsonObject.getString("deviceSn");
                String url = jsonObject.getString("url");
                String totalSize = jsonObject.getString("totalSize");
                String completedSize = jsonObject.getString("completedSize");
                String fileName = jsonObject.getString("fileName");
                String realName = jsonObject.getString("realName");
                DeviceLofInfoRes deviceLofInfoRes = new DeviceLofInfoRes();
                deviceLofInfoRes.setTaskId(id);
                deviceLofInfoRes.setTaskStatus(status);
                deviceLofInfoRes.setTaskCode(msgcode);
                deviceLofInfoRes.setDeviceSn(deviceid);
                deviceLofInfoRes.setUrl(url);
                deviceLofInfoRes.setFileName(fileName);
                deviceLofInfoRes.setRealName(realName);
                deviceLofInfoRes.setTotalSize(totalSize);
                deviceLofInfoRes.setCompletedSize(completedSize);
                iotDeviceLogService.getDeviceLogToCloud(deviceLofInfoRes);

            } else if (DeviceConstants.DEVICE_CHANNL_4001.equals(msgcode)) {
                JSONObject alarmInfo = jsonObject.getJSONObject("alarm_info");
                AlarmInfoRes alarmInfoRes = new AlarmInfoRes();
                alarmInfoRes.setDeviceId(jsonObject.getLong("deviceid"));
                alarmInfoRes.setTaskId(id);
                alarmInfoRes.setTaskStatus(status);
                alarmInfoRes.setTaskCode(msgcode);
                alarmInfoRes.setEthernet(alarmInfo.getInteger("ethernet"));
                alarmInfoRes.setModem(alarmInfo.getInteger("modem"));
                alarmInfoRes.setRam(alarmInfo.getInteger("ram"));
                alarmInfoRes.setData(alarmInfo.getInteger("data"));
                alarmInfoRes.setSimCard(alarmInfo.getInteger("simCard"));
                iotDeviceAlarmService.getDeviceAlarmToCloud(alarmInfoRes);
            }
        }
    }
}
