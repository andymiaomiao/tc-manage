package com.opencloud.device.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.IotDeviceLog;
import com.opencloud.device.entity.IotDeviceTask;
import com.opencloud.device.model.DeviceConstants;
import com.opencloud.device.model.MqttEnum;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.ResultBody;
//import com.opencloud.device.mqtt.MqttMessageSender;
import com.opencloud.device.properties.DeviceManageProperties;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.security.ServerConfiguration;
import com.opencloud.device.service.DiskFileService;
import com.opencloud.device.service.IotDeviceLogService;
import com.opencloud.device.service.IotDeviceTaskService;
import com.opencloud.device.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Api(tags = "设备日志")
@RestController
@AllArgsConstructor
public class IotDeviceLogController {

    private DeviceManageProperties deviceManageProperties;

    private IotDeviceTaskService iotDeviceTaskService;

    private ServerConfiguration serverConfiguration;

    private IotDeviceLogService iotDeviceLogService;

//    private MqttMessageSender mqttMessageSender;

    private DiskFileService diskFileService;

    private RedisUtil redisUtil;

    /**
     * 获取功能操作列表
     *
     * @param map
     * @return
     */
    @GetMapping("/log/page")
    public ResultBody<Page<Map<String, Object>>> getPage(@RequestParam Map<String, Object> map) {
        return ResultBody.ok().data(iotDeviceLogService.findPage(new PageParams(map)));
    }

    /**
     * 获取日志进度详情
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "获取日志进度详情", notes = "获取日志进度详情")
    @GetMapping("/log/progress")
    public ResultBody getProgress(@RequestParam("deviceId") Long deviceId,@RequestParam("deviceSn") String deviceSn) {
        Object o = redisUtil.get("SOCKET_SAVE_" + deviceSn);
        System.out.println(deviceSn);
        if(o!=null) {
            System.out.println(o.toString());
            JSONObject jsonObject = JSON.parseObject((String) o);
            Integer fileCountSize = jsonObject.getInteger("fileCountSize");
            Integer fileReadSize = jsonObject.getInteger("fileReadSize");
            BigDecimal uploadProgress;
            Integer uploadStatus = 0;
            if(fileReadSize>=fileCountSize){
                uploadProgress = new BigDecimal("100");
                uploadStatus = 1;
                redisUtil.del("SOCKET_SAVE_" + deviceSn);
            }else {
                uploadProgress = new BigDecimal(fileReadSize).divide(new BigDecimal(fileCountSize),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                uploadStatus = 0;
            }
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("uploadProgress",uploadProgress.setScale(1,BigDecimal.ROUND_HALF_UP));
            jsonObject.put("uploadStatus",uploadStatus);
            return ResultBody.ok().data(jsonObject);
        }
        return ResultBody.ok().data(null);
    }



    /**
     * 日志下载
     *
     * @param deviceSn
     * @param deviceId
     * @return
     */
    @OperationLog(value = "日志上传")
    @ApiOperation(value = "日志上传", notes = "日志上传")
    @GetMapping("/log/upload/to/device")
    public ResultBody uploadToDevice(@RequestParam("deviceSn") String deviceSn,@RequestParam("deviceId") Long deviceId) {
        IotDeviceTask iotDeviceTask = new IotDeviceTask();
        iotDeviceTask.setDeviceId(deviceId);
        iotDeviceTask.setDeviceSn(deviceSn);
        iotDeviceTask.setCode(DeviceConstants.DEVICE_CHANNL_3001);
        iotDeviceTask.setStatus(MqttEnum.DEVICE_ACK.status);
        IotDeviceTask save = iotDeviceTaskService.add(iotDeviceTask);
        if (save != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", save.getTaskId().toString());
            if(deviceManageProperties.getSave().getHost().equals("127.0.0.1")) {
                jsonObject.put("ip", serverConfiguration.getHostAddress());
            }else{
                jsonObject.put("ip", deviceManageProperties.getSave().getHost());
            }
            System.out.println(deviceManageProperties.getSave().getHost());
            jsonObject.put("port",deviceManageProperties.getSave().getPort());
            System.out.println(deviceManageProperties.getSave().getPort());
            jsonObject.put("msgcode", DeviceConstants.DEVICE_CHANNL_3001);
//            mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_LOG + deviceSn, jsonObject.toJSONString());
        }
        return ResultBody.ok();
    }

    @OperationLog(value = "日志批量上传")
    @ApiOperation(value = "日志批量上传", notes = "日志批量上传")
    @PostMapping("/log/upload/to/batch")
    public ResultBody uploadBatch(@RequestParam("deviceList") String deviceList){
        JSONArray deviceObjList = JSON.parseArray(deviceList);
        if(deviceObjList!=null&&deviceObjList.size()>0){
            for (Object item : deviceObjList) {
                JSONObject item1 = (JSONObject) item;
                this.uploadToDevice(String.valueOf(item1.get("deviceSn")), Long.valueOf(String.valueOf(item1.get("deviceId"))));
            }
        }
        return ResultBody.ok();
    }

    @OperationLog(value = "日志批量打包")
    @ApiOperation(value = "日志批量打包", notes = "日志批量打包")
    @PostMapping("/log/pack/batch")
    public ResultBody packBatch(@RequestParam("deviceList") String deviceList){
        return ResultBody.ok();
    }

//    /**
//     * 获取功能按钮详情
//     *
//     * @param deviceId
//     * @return
//     */
//    @GetMapping("/log/info")
//    public ResultBody<IotDevice> get(@RequestParam("deviceId") Long deviceId) {
//        return iotDeviceLogService.get(deviceId);
//    }

//    /**
//     * 添加/修改功能按钮
//     *
//     * @return
//     */
//    @PostMapping("/device/save")
//    public ResultBody<Long> save(
//            @RequestParam(value = "deviceId") Long deviceId,
//            @RequestParam(value = "distribuId") Long distribuId,
//            @RequestParam(value = "deviceName") String deviceName,
//            @RequestParam(value = "deviceIp", required = false) String deviceIp,
//            @RequestParam(value = "deviceSn") String deviceSn,
//            @RequestParam(value = "protocol", required = false) String protocol,
//            @RequestParam(value = "online", defaultValue = "0", required = false) Integer online,
//            @RequestParam(value = "secrecyLevel",  defaultValue = "1", required = false) Integer secrecyLevel,
//            @RequestParam(value = "tags", required = false) String tags,
//            @RequestParam(value = "lat", defaultValue = "0.00", required = false) BigDecimal lat,
//            @RequestParam(value = "lon", defaultValue = "0.00", required = false) BigDecimal lon,
//            @RequestParam(value = "softwareVersion", required = false) String softwareVersion,
//            @RequestParam(value = "distribuImgX", defaultValue = "0.00", required = false) BigDecimal distribuImgX,
//            @RequestParam(value = "distribuImgY", defaultValue = "0.00", required = false) BigDecimal distribuImgY,
//            @RequestParam(value = "distribuImgW", defaultValue = "0.00", required = false) BigDecimal distribuImgW,
//            @RequestParam(value = "distribuImgH", defaultValue = "0.00", required = false) BigDecimal distribuImgH,
//            @RequestParam(value = "upgradeProgress", defaultValue = "0.00", required = false) BigDecimal upgradeProgress,
//            @RequestParam(value = "upgradeStatus", defaultValue = "1", required = false) Integer upgradeStatus,
//            @RequestParam(value = "ethernet", defaultValue = "", required = false) Integer ethernet,
//            @RequestParam(value = "modem", defaultValue = "1", required = false) Integer modem,
//            @RequestParam(value = "ram", defaultValue = "1", required = false) Integer ram,
//            @RequestParam(value = "data", defaultValue = "1", required = false) Integer data,
//            @RequestParam(value = "simCard", defaultValue = "1", required = false) Integer simCard
//    ) {
//        IotDevice device = new IotDevice();
//        device.setTenantId(SecurityHelper.getUser().getTenantId());
//        device.setDeviceId(deviceId);
//        device.setDistribuId(distribuId);
//        device.setDeviceName(deviceName);
//        device.setDeviceIp(deviceIp);
//        device.setDeviceSn(deviceSn);
//        device.setProtocol(protocol);
//        device.setOnline(online);
//        device.setSecrecyLevel(secrecyLevel);
//        device.setTags(tags);
//        device.setLat(lat);
//        device.setLon(lon);
//        device.setSoftwareVersion(softwareVersion);
//        device.setDistribuImgX(distribuImgX);
//        device.setDistribuImgY(distribuImgY);
//        device.setDistribuImgW(distribuImgW);
//        device.setDistribuImgH(distribuImgH);
//        device.setUpgradeProgress(upgradeProgress);
//        device.setUpgradeStatus(upgradeStatus);
//        device.setEthernet(ethernet);
//        device.setModem(modem);
//        device.setRam(ram);
//        device.setData(data);
//        device.setSimCard(simCard);
//        iotDeviceService.save(device);
//        return ResultBody.ok();
//    }

    /**
     * 移除功能按钮
     *
     * @return
     */
    @PostMapping("/log/remove/batch")
    public ResultBody removeBatch(@RequestParam("logList") String logList) {
        List<IotDeviceLog> iotDeviceLogs = JSONObject.parseArray(logList, IotDeviceLog.class);
        if(iotDeviceLogs!=null&&iotDeviceLogs.size()>0){
            List<Long> logIds = new ArrayList<>();
            List<Map<String, Object>> resultLogMap = new ArrayList<>();
            iotDeviceLogs.stream().forEach(item->{
                Map<String, Object> map1 = Maps.newHashMap();
                logIds.add(item.getLogId());
                Map<String, Object> map = Maps.newHashMap();
                map.put("fileName", item.getFileName());
                map.put("realName", item.getRealName());
                resultLogMap.add(map);
            });
            if(logIds.size()>0) {
                iotDeviceLogService.removeByIds(logIds);
            }
            if(resultLogMap.size()>0){
                diskFileService.removeBatch(resultLogMap);
            }
        }

        return ResultBody.ok();
    }
//
//
//    @ApiOperation(value = "获取设备信息", notes = "获取设备信息")
//    @GetMapping("/device/info/to/device")
//    public ResultBody getAlarmRepairToDevice(@RequestParam String deviceSn,@RequestParam Long deviceId) {
//        IotDeviceTask iotDeviceTask = new IotDeviceTask();
//        iotDeviceTask.setDeviceId(deviceId);
//        iotDeviceTask.setDeviceSn(deviceSn);
//        iotDeviceTask.setCode(DeviceConstants.DEVICE_CHANNL_1001);
//        iotDeviceTask.setStatus(MqttEnum.DEVICE_ACK.status);
//        ResultBody<Long> save = iotDeviceTaskService.save(iotDeviceTask);
//        if (save != null && save.getCode() == 0) {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("id", save.getData());
//            jsonObject.put("deviceid", deviceSn);
//            jsonObject.put("msgcode", DeviceConstants.DEVICE_CHANNL_1001);
//            mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_INFO + deviceSn, jsonObject.toJSONString());
//        }
//        return ResultBody.ok();
//    }
//
//    @GetMapping("/device/info/dashboard")
//    public ResultBody<Map<String, Object>> getDeviceInfoDashboard(){
//        return iotDeviceService.getDeviceInfoDashboard(SecurityHelper.getUser().getTenantId());
//    }
//
//
//    @ApiOperation(value = "上传文件", notes = "上传文件")
//    @PostMapping(value="/device/upgrade/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResultBody upgradeFileUpload(@RequestParam(value = "file",required = false) MultipartFile file, HttpServletRequest request){
//        return ossFileUploadService.handleFileUpload(file,"device-manage",SecurityHelper.getUser().getUsername(),"dffewf",SecurityHelper.getUser().getTenantId());
//    }
}
