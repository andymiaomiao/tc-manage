package com.opencloud.device.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.converter.Custemhandler;
import com.opencloud.device.converter.IotDeviceExcel;
import com.opencloud.device.converter.IotDeviceWriteHandler;
import com.opencloud.device.entity.*;
import com.opencloud.device.form.req.DeviceBatchRes;
import com.opencloud.device.listener.IotDeviceListener;
import com.opencloud.device.model.*;
//import com.opencloud.device.mqtt.MqttMessageSender;
import com.opencloud.device.properties.DeviceManageProperties;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.security.ServerConfiguration;
import com.opencloud.device.service.*;
import com.opencloud.device.utils.RedisUtil;
import com.opencloud.device.utils.ipUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuyadu
 */
@Api(tags = "设备中心")
@RestController
@AllArgsConstructor
@Slf4j
public class IotDeviceController{

    private DeviceManageProperties deviceManageProperties;

    private IotDeviceTaskService iotDeviceTaskService;

    private ServerConfiguration serverConfiguration;

//    private MqttMessageSender mqttMessageSender;

    private IotDeviceService iotDeviceService;

    private DiskFileService diskFileService;

    private RedisUtil redisUtil;

    /**
     * 获取功能操作列表
     *
     * @param map
     * @return
     */
    @GetMapping("/device/page")
    public ResultBody<Page<IotDevice>> getPage(@RequestParam Map<String, Object> map) {
        return ResultBody.ok().data(iotDeviceService.findPage(new PageParams(map)));
    }

    /**
     * 获取所有设备列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有角色列表", notes = "获取所有角色列表")
    @GetMapping("/device/list")
    public ResultBody<List<IotDevice>> getList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(iotDeviceService.findList(new RequestParams(map)));
    }

    /**
     * 获取功能按钮详情
     *
     * @param deviceId
     * @return
     */
    @GetMapping("/device/info")
    public ResultBody<IotDevice> get(@RequestParam("deviceId") Long deviceId) {
        return ResultBody.ok().data(iotDeviceService.getById(deviceId));
    }

    /**
     * 获取设备进度详情
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "设备升级进度", notes = "设备升级进度")
    @GetMapping("/device/progress")
    public ResultBody getprogress(@RequestParam("deviceId") Long deviceId,@RequestParam("deviceSn") String deviceSn) {
        Object o = redisUtil.get("SOCKET_TRACE_" + deviceSn);
        if(o!=null) {
            System.out.println(o.toString());
            JSONObject jsonObject = JSON.parseObject((String) o);
            Integer fileCountSize = jsonObject.getInteger("fileCountSize");
            Integer fileReadSize = jsonObject.getInteger("fileReadSize");
            BigDecimal upgradeProgress;
            Integer upgradeStatus = 0;
            if(fileReadSize>=fileCountSize){
                upgradeProgress = new BigDecimal("100");
                upgradeStatus = 1;
                redisUtil.del("SOCKET_TRACE_" + deviceSn);
            }else {
                upgradeProgress = new BigDecimal(fileReadSize).divide(new BigDecimal(fileCountSize),4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                upgradeStatus = 0;
            }
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("upgradeProgress",upgradeProgress.setScale(1,BigDecimal.ROUND_HALF_UP));
            jsonObject.put("upgradeStatus",upgradeStatus);
            return ResultBody.ok().data(jsonObject);
        }
        return ResultBody.ok().data(null);
    }

    /**
     * 获取设备进度详情
     *
     * @param deviceList
     * @return
     */
    @ApiOperation(value = "设备升级进度", notes = "设备升级进度")
    @GetMapping("/device/interval")
    public ResultBody getInterval(@RequestParam("deviceList") String deviceList) {
        List<JSONObject> collect = new ArrayList<>();
        if(StringUtils.isNotBlank(deviceList)){
            JSONArray jsonArray = JSON.parseArray(deviceList);
            if(jsonArray!=null&&jsonArray.size()>0) {
                collect.addAll(jsonArray.stream().map(m -> {
                    JSONObject jsonObj = (JSONObject) m;
                    log.info(jsonObj.toJSONString());
                    Object deviceSnStatus = redisUtil.get(DeviceRedisKey.DEVICE_REDIS_STATUS + jsonObj.getString("deviceSn"));
                    if (deviceSnStatus != null) {
                        jsonObj.put("online", deviceSnStatus);
                    }
                    Object deviceIdStatus = redisUtil.get(DeviceRedisKey.DEVICE_REDIS_STATUS + jsonObj.getString("deviceId"));
                    if (deviceIdStatus != null) {
                        jsonObj.put("online", deviceIdStatus);
                    }
                    return jsonObj;
                }).collect(Collectors.toList()));
            }
        }
        return ResultBody.ok().data(collect);
    }

    /**
     * 添加/修改功能按钮
     *
     * @return
     */
    @OperationLog(value = "添加/修改设备")
    @ApiOperation(value = "添加/修改设备", notes = "添加设备")
    @PostMapping("/device/save")
    public ResultBody<Long> save(
            @RequestParam(value = "deviceId") Long deviceId,
//            @RequestParam(value = "distribuId") Long distribuId,
//            @RequestParam(value = "categoryId") Long categoryId,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "deviceName") String deviceName,
            @RequestParam(value = "deviceBug") String deviceBug,
            @RequestParam(value = "deviceNew") String deviceNew,
            @RequestParam(value = "deviceRom") String deviceRom,
            @RequestParam(value = "deviceIp", required = false) String deviceIp,
            @RequestParam(value = "deviceSn") String deviceSn,
            @RequestParam(value = "protocol", required = false) String protocol,
            @RequestParam(value = "online", defaultValue = "0", required = false) Integer online,
            @RequestParam(value = "secrecyLevel", defaultValue = "1", required = false) Integer secrecyLevel,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "softwareVersion", required = false) String softwareVersion,
            @RequestParam(value = "distribuImgX", defaultValue = "0.00", required = false) Double distribuImgX,
            @RequestParam(value = "distribuImgY", defaultValue = "0.00", required = false) Double distribuImgY,
            @RequestParam(value = "distribuImgW", defaultValue = "0.00", required = false) Double distribuImgW,
            @RequestParam(value = "distribuImgH", defaultValue = "0.00", required = false) Double distribuImgH,
            @RequestParam(value = "upgradeProgress", defaultValue = "0.00", required = false) Double upgradeProgress,
            @RequestParam(value = "upgradeStatus", defaultValue = "1", required = false) Integer upgradeStatus,
            @RequestParam(value = "uploadProgress", defaultValue = "0.00", required = false) Double uploadProgress,
            @RequestParam(value = "uploadStatus", defaultValue = "1", required = false) Integer uploadStatus,
            @RequestParam(value = "ethernet", defaultValue = "", required = false) Integer ethernet,
            @RequestParam(value = "modem", defaultValue = "1", required = false) Integer modem,
            @RequestParam(value = "ram", defaultValue = "1", required = false) Integer ram,
            @RequestParam(value = "data", defaultValue = "1", required = false) Integer data,
            @RequestParam(value = "simCard", defaultValue = "1", required = false) Integer simCard
    ) {
        IotDevice device = new IotDevice();
        device.setDeviceBug(deviceBug);
        device.setDeviceNew(deviceNew);
        device.setDeviceRom(deviceRom);
        device.setDeviceId(deviceId);
//        device.setCategoryId(categoryId);
        device.setCategoryName(categoryName);
//        device.setDistribuId(distribuId);
        device.setDeviceName(deviceName);
        device.setDeviceIp(deviceIp);
        device.setDeviceSn(deviceSn);
        device.setProtocol(protocol);
        device.setOnline(online);
        device.setSecrecyLevel(secrecyLevel);
        device.setTags(tags);
        device.setSoftwareVersion(softwareVersion);
        device.setDistribuImgX(distribuImgX);
        device.setDistribuImgY(distribuImgY);
        device.setDistribuImgW(distribuImgW);
        device.setDistribuImgH(distribuImgH);
        device.setUpgradeProgress(upgradeProgress);
        device.setUpgradeStatus(upgradeStatus);
        device.setUploadProgress(uploadProgress);
        device.setUploadStatus(uploadStatus);
        device.setEthernet(ethernet);
        device.setModem(modem);
        device.setRam(ram);
        device.setData(data);
        device.setSimCard(simCard);
        IotDevice add;
        if(deviceId!=null){
            add = iotDeviceService.update(device);
        }else {
            add = iotDeviceService.add(device);
        }
        return ResultBody.ok().data(add.getDeviceId());
    }

    /**
     * 添加/修改功能按钮
     *
     * @return
     */
    @OperationLog(value = "调整设备分布地")
    @ApiOperation(value = "调整设备分布地", notes = "调整设备分布地")
    @PostMapping("/device/adjust/save")
    public ResultBody<Long> adjustSave(
            @RequestParam(value = "deviceId") Long deviceId,
            @RequestParam(value = "distribuImgX", defaultValue = "0.00", required = false) Double distribuImgX,
            @RequestParam(value = "distribuImgY", defaultValue = "0.00", required = false) Double distribuImgY,
            @RequestParam(value = "distribuImgW", defaultValue = "0.00", required = false) Double distribuImgW,
            @RequestParam(value = "distribuImgH", defaultValue = "0.00", required = false) Double distribuImgH
    ){
        IotDevice device = new IotDevice();
        device.setDeviceId(deviceId);
        device.setDistribuImgX(distribuImgX);
        device.setDistribuImgY(distribuImgY);
        device.setDistribuImgW(distribuImgW);
        device.setDistribuImgH(distribuImgH);
        if (device.getDeviceId() != null) {
            iotDeviceService.adjustUpdate(device);
        }
        return ResultBody.ok();
    }

    /**
     * 移除功能按钮
     *
     * @param deviceId
     * @return
     */
    @OperationLog(value = "移除设备")
    @ApiOperation(value = "移除设备", notes = "移除设备")
    @PostMapping("/device/remove")
    public ResultBody remove(@RequestParam("deviceId") Long deviceId) {
        iotDeviceService.remove(deviceId);
        return ResultBody.ok();
    }

    /**
     * 移除功能按钮
     *
     * @param map
     * @return
     */
    @OperationLog(value = "批量移除设备")
    @ApiOperation(value = "批量移除设备", notes = "批量移除设备")
    @PostMapping("/device/batch/remove")
    public ResultBody batchRemove(@RequestParam(required = false) Map map) {
        iotDeviceService.batchRemove(new RequestParams(map));
        return ResultBody.ok();
    }

    @ApiOperation(value = "获取设备MQTT信息", notes = "获取设备MQTT信息")
    @GetMapping("/device/info/to/device")
    public ResultBody getAlarmRepairToDevice(@RequestParam String deviceSn, @RequestParam Long deviceId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", IdWorker.getIdStr());
        jsonObject.put("msgcode", DeviceConstants.DEVICE_CHANNL_1001);
//        mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_INFO + deviceSn, jsonObject.toJSONString());
        return ResultBody.ok();
    }

    @GetMapping("/device/info/dashboard")
    public ResultBody<Map<String, Object>> getDeviceInfoDashboard() {
        return ResultBody.ok().data(iotDeviceService.getDeviceInfoDashboard());
    }

    @OperationLog(value = "设备升级")
    @ApiOperation(value = "设备升级", notes = "设备升级")
    @PostMapping(value = "/device/soft/to/device")
    public ResultBody upgradeFileUpload(@RequestParam("deviceSn") String deviceSn, @RequestParam("deviceId") Long deviceId, @RequestParam("filesId") Long filesId,DiskFile diskFile) {
        if(diskFile==null||(diskFile!=null &&diskFile.getFileId()==null)) {
            DiskFile diskFileInfo = diskFileService.getById(filesId);
            if (diskFileInfo != null) {
                diskFile = diskFileInfo;
            }
        }else{
//            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
        List<JSONObject> jsonObjects = new ArrayList<>();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("deviceid", deviceSn);
        jsonObject1.put("version", diskFile.getFileVersion());
        jsonObject1.put("filename", diskFile.getNowName());
        jsonObject1.put("filepath", "/var");
        jsonObject1.put("filesize", diskFile.getFileSize());
        jsonObject1.put("IP", deviceManageProperties.getTrace().getHost());
        System.out.println(deviceManageProperties.getTrace().getHost());
        System.out.println("================================"+deviceManageProperties.getTrace().getHost());
        log.info("================================"+deviceManageProperties.getTrace().getHost());
        jsonObject1.put("port", deviceManageProperties.getTrace().getPort());
        System.out.println(deviceManageProperties.getTrace().getPort());
        System.out.println("================================"+deviceManageProperties.getTrace().getPort());
        log.info("================================"+deviceManageProperties.getTrace().getPort());
        jsonObjects.add(jsonObject1);
        IotDeviceTask iotDeviceTask = new IotDeviceTask();
        iotDeviceTask.setDeviceId(deviceId);
        iotDeviceTask.setDeviceSn(deviceSn);
        iotDeviceTask.setCode(DeviceConstants.DEVICE_CHANNL_2001);
        iotDeviceTask.setStatus(MqttEnum.DEVICE_ACK.status);
        IotDeviceTask save = iotDeviceTaskService.add(iotDeviceTask);
        if (save != null) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", save.getTaskId().toString());
            jsonObject.put("msg", DeviceConstants.DEVICE_CHANNL_2001);
            jsonObject.put("msgbody", jsonObjects);
            log.info(jsonObject.toJSONString());
//            mqttMessageSender.sendToMqtt(DeviceConstants.CLOUD_DEVICE_SOFT + deviceSn, jsonObject.toJSONString());
        }
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("deviceId", deviceId);
        jsonObject.put("deviceSn", deviceSn);
        jsonArray.add(jsonObject);
        return ResultBody.ok().data(jsonArray);
    }

    @OperationLog(value = "设备批量升级")
    @ApiOperation(value = "设备批量升级", notes = "设备批量升级")
    @PostMapping(value = "/device/soft/to/batch")
    public ResultBody uploadBatch(@RequestParam("deviceList") String deviceList, @RequestParam("filesId") Long filesId) {
        List<JSONObject> jsonObjects = new ArrayList<>();
        DiskFile diskFileInfo = diskFileService.getById(filesId);
        if (diskFileInfo != null) {
            JSONArray deviceObjList = JSON.parseArray(deviceList);
            if (deviceObjList != null && deviceObjList.size() > 0) {
                for (Object item : deviceObjList) {
                    JSONObject item1 = (JSONObject) item;
                    jsonObjects.add(item1);
                    upgradeFileUpload(String.valueOf(item1.get("deviceSn")),Long.valueOf(String.valueOf(item1.get("deviceId"))),filesId,diskFileInfo);
                }
            }
        }
        return ResultBody.ok().data(jsonObjects);
    }

    @ApiOperation(value="外部接口获取信息")
    @GetMapping("/api/device/list")
    public ResultBody<Map<String,Object>> getDeviceList() {
        return ResultBody.ok().data(iotDeviceService.getDeviceList());
    }

    /**
     * 模版导出
     * @throws IOException
     */
    @GetMapping("/device/template/download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("导入模版"+ IdWorker.getIdStr(), "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), IotDeviceExcel.class)
                .registerWriteHandler(new IotDeviceWriteHandler())
                .registerWriteHandler(new Custemhandler()).sheet("模板").doWrite(iotDeviceData());
    }

    /**
     * 模版导入
     * @param deviceList
     * @return
     * @throws IOException
     */
    @PostMapping("/device/template/export")
    public ResultBody<List<IotDevice>> upload(@RequestParam("deviceList") String deviceList) {
        if(deviceList!=null) {
            List<IotDevice> iotDeviceList = new ArrayList<>();
            JSONArray deviceObjList = JSON.parseArray(deviceList);
            List<String> deviceSnList = new ArrayList<>();
            if (deviceObjList.size() > 0) {
                deviceObjList.stream().forEach(item -> {
                    JSONObject item1 = (JSONObject) item;
                    IotDevice iotDevice = new IotDevice();
                    iotDevice.setDeviceSn(item1.getString("deviceSn"));
                    iotDevice.setDeviceName(item1.getString("deviceName"));
                    iotDevice.setCategoryName(item1.getString("categoryName"));
                    iotDevice.setCategoryId(Long.valueOf(String.valueOf(item1.getString("categoryId"))));
                    iotDevice.setDistribuName(item1.getString("distribuName"));
                    iotDevice.setDistribuId(Long.valueOf(String.valueOf(item1.getString("distribuId"))));
                    iotDevice.setDistribuImgX(0d);
                    iotDevice.setDistribuImgY(0d);
                    iotDevice.setTenantId(SecurityHelper.getUser().getTenantId());
                    iotDevice.setOnline(0);
                    iotDeviceList.add(iotDevice);
                    deviceSnList.add(item1.getString("deviceSn"));
                });
            }
            List<IotDevice> deviceSaveBatch = new ArrayList<>();
            List<IotDevice> deviceExistBatch = new ArrayList<>();
            if(deviceSnList!=null&&deviceSnList.size()>0) {
                // 查询是否有重复的设备
                QueryWrapper<IotDevice> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().in(IotDevice::getDeviceSn,deviceSnList);
                List<IotDevice> deviceDupList = iotDeviceService.list(queryWrapper);
                if(deviceDupList!=null&&deviceDupList.size()>0){
                    iotDeviceList.stream().forEach(m->{
                        Optional<IotDevice> first = deviceDupList.stream().filter(f -> !f.getDeviceSn().equals(m.getDeviceSn())).findFirst();
                        if(first.isPresent()){
                            deviceExistBatch.add(m);
                        }else{
                            deviceSaveBatch.add(m);
                        }
                    });
                }else{
                    deviceSaveBatch.addAll(iotDeviceList);
                }
            }
            iotDeviceService.saveBatch(deviceSaveBatch);
            if(deviceExistBatch!=null&&deviceExistBatch.size()>0){
                return ResultBody.ok().data(deviceExistBatch);
            }
        }
        return ResultBody.ok();
//        EasyExcel.read(file.getInputStream(), IotDevice.class, new IotDeviceListener(iotDeviceServiceClient)).sheet().doRead();
//        return ResultBody.ok();
    }

    private IotDeviceDistributionService iotDeviceDistributionService;

    private SystemCategoryService systemCategoryService;

    private List<IotDeviceExcel> iotDeviceData() {
        List<IotDeviceExcel> list = new ArrayList<>();
        Long tenantId = SecurityHelper.getUser().getTenantId();
        Map<String,Object> requestMap = new HashMap<>(16);
        requestMap.put("tenantId",tenantId);
        List<IotDeviceDistribution> distributionBody = iotDeviceDistributionService.findList(new RequestParams(requestMap));
        List<IotDeviceDistribution> distributionData = new ArrayList<>();
        if(distributionBody!=null&&distributionBody.size()>0){
            distributionData.addAll(distributionBody);
        }
        List<SystemCategory> categoryBody = systemCategoryService.findList(new RequestParams(requestMap));
        List<SystemCategory> categoryList = new ArrayList<>();
        if(categoryBody!=null&&categoryBody.size()>0){
            categoryList.addAll(categoryBody);
        }
        if(distributionData.size()>=categoryList.size()){
            for (int i = 0; i < distributionData.size(); i++) {
                IotDeviceExcel data = new IotDeviceExcel();
                data.setDeviceSn("CP335C_");
                data.setDistribuId(distributionData.get(i).getDistributionId());
                data.setDistribuName(distributionData.get(i).getDistributionName());
                if(categoryList.size()>=(i+1)){
                    data.setCategoryId(categoryList.get(i).getCategoryId());
                    data.setCategoryName(categoryList.get(i).getName());
                }
                data.setDistribuImgX(BigDecimal.ZERO);
                data.setDistribuImgY(BigDecimal.ZERO);
                list.add(data);
            }
        }else if(distributionData.size()<categoryList.size()){
            for (int i = 0; i < categoryList.size(); i++) {
                IotDeviceExcel data = new IotDeviceExcel();
                data.setDeviceSn("CP335C_");
                data.setCategoryId(categoryList.get(i).getCategoryId());
                data.setCategoryName(categoryList.get(i).getName());
                if(distributionData.size()>=(i+1)){
                    data.setDistribuId(distributionData.get(i).getDistributionId());
                    data.setDistribuName(distributionData.get(i).getDistributionName());
                }
                data.setDistribuImgX(BigDecimal.ZERO);
                data.setDistribuImgY(BigDecimal.ZERO);
                list.add(data);
            }
        }
        return list;
    }
}
