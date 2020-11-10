package com.opencloud.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.entity.IotDeviceAlarm;
import com.opencloud.device.entity.IotDeviceTask;
import com.opencloud.device.model.DeviceConstants;
import com.opencloud.device.model.MqttEnum;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.mqtt.MqttMessageSender;
import com.opencloud.device.service.IotDeviceAlarmService;
import com.opencloud.device.service.IotDeviceService;
import com.opencloud.device.service.IotDeviceTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-16 09:49
 */
@Api(tags = "告警中心")
@RestController
public class IotDeviceAlarmController {

    @Autowired
    private IotDeviceAlarmService iotDeviceAlarmService;

    @Autowired
    private MqttMessageSender mqttMessageSender;

    @Autowired
    private IotDeviceService iotDeviceService;

    @Autowired
    private IotDeviceTaskService iotDeviceTaskService;

    /**
     * 获取功能操作列表
     *
     * @param map
     * @return
     */
    @GetMapping("/alarm/page")
    public ResultBody<Page<IotDeviceAlarm>> getPage(@RequestParam Map<String, Object> map) {
        return ResultBody.ok().data(iotDeviceAlarmService.findPage(new PageParams(map)));
    }

    @OperationLog(value = "修复告警信息")
    @ApiOperation(value = "修复告警信息", notes = "修复告警信息")
    @GetMapping("/alarm/repair/to/device")
    public ResultBody getAlarmRepairToDevice(@RequestParam String deviceSn,@RequestParam Long deviceId){
        IotDevice iotDevice = iotDeviceService.getById(deviceId);
        if(iotDevice!=null){
            IotDeviceTask iotDeviceTask = new IotDeviceTask();
            iotDeviceTask.setDeviceId(deviceId);
            iotDeviceTask.setDeviceSn(deviceSn);
            iotDeviceTask.setCode(DeviceConstants.DEVICE_CHANNL_4001);
            iotDeviceTask.setStatus(MqttEnum.DEVICE_ACK.status);
            IotDeviceTask save = iotDeviceTaskService.add(iotDeviceTask);
            if(save!=null){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",save.getTaskId());
                jsonObject.put("deviceid",iotDevice.getDeviceSn());
                jsonObject.put("msgcode", DeviceConstants.DEVICE_CHANNL_4001);
                JSONObject alarmInfo =new JSONObject();
                alarmInfo.put("ethernet",iotDevice.getEthernet()+"");
                alarmInfo.put("modem",iotDevice.getModem()+"");
                alarmInfo.put("memory",iotDevice.getRam()+"");
                alarmInfo.put("data",iotDevice.getData()+"");
                alarmInfo.put("sim_card",iotDevice.getSimCard()+"");
                jsonObject.put("alarm_info",alarmInfo);
                mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_ALARM+iotDevice.getDeviceSn(),jsonObject.toJSONString());
            }
        }
        return ResultBody.ok();
    }
}
