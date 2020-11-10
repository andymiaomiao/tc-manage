package com.opencloud.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Maps;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.entity.IotDeviceTask;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.form.req.DeviceBatchRes;
import com.opencloud.device.form.req.DeviceInfoRes;
import com.opencloud.device.mapper.IotDeviceMapper;
import com.opencloud.device.model.DeviceRedisKey;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.IotDeviceService;
import com.opencloud.device.service.IotDeviceTaskService;
import com.opencloud.device.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 通用账号
 *
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IotDeviceServiceImpl extends BaseServiceImpl<IotDeviceMapper, IotDevice> implements IotDeviceService {

    @Autowired
    private IotDeviceTaskService iotDeviceTaskService;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public IPage<IotDevice> findPage(PageParams pageParams) {
        IotDevice query = pageParams.mapToBean(IotDevice.class);
        QueryWrapper<IotDevice> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getOnline()), IotDevice::getOnline, query.getOnline())
                .and(ObjectUtils.isNotEmpty(query.getDeviceName()) || ObjectUtils.isNotEmpty(query.getDeviceSn()),
                        wrapper ->
                                wrapper
                                        .like(ObjectUtils.isNotEmpty(query.getDeviceName()), IotDevice::getDeviceName, query.getDeviceName())
                                        .or()
                                        .like(ObjectUtils.isNotEmpty(query.getDeviceSn()), IotDevice::getDeviceSn, query.getDeviceSn())
                )
                .orderByAsc(IotDevice::getCreateTime,IotDevice::getDeviceId);
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询设备列表
     *
     * @return
     */
    @Override
    public List<IotDevice> findList(RequestParams requestParams) {
        IotDevice query = requestParams.mapToBean(IotDevice.class);
        QueryWrapper<IotDevice> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getOnline()), IotDevice::getOnline, query.getOnline())
                .eq(ObjectUtils.isNotEmpty(query.getCategoryId()), IotDevice::getCategoryId, query.getCategoryId())
                .eq(ObjectUtils.isNotEmpty(query.getDistribuId()), IotDevice::getDistribuId, query.getDistribuId());
        List<IotDevice> list = list(queryWrapper);
        return list;
    }

    /**
     * 添加菜单资源
     *
     * @param device
     * @return
     */
    @Override
    public IotDevice add(IotDevice device) {
        if (isExist(device.getDeviceSn())) {
            throw new BaseException(String.format("%s编码已存在!", device.getDeviceSn()));
        }
//        if (device.getParentId() == null) {
//            device.setParentId(0L);
//        }
//        if (device.getPriority() == null) {
//            device.setPriority(0);
//        }
//        if (device.getStatus() == null) {
//            device.setStatus(1);
//        }
//        if (device.getIsPersist() == null) {
//            device.setIsPersist(0);
//        }
        device.setCreateTime(new Date());
        device.setUpdateTime(device.getCreateTime());
        save(device);
        // 同步权限表里的信息
        return device;
    }

    /**
     * 修改菜单资源
     *
     * @param device
     * @return
     */
    @Override
    public IotDevice update(IotDevice device) {
        IotDevice saved = getById(device.getDeviceId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在!", device.getDeviceId()));
        }
        if (!saved.getDeviceSn().equals(device.getDeviceSn())) {
            // 和原来不一致重新检查唯一性
            if (isExist(device.getDeviceSn())) {
                throw new BaseException(String.format("%s编码已存在!", device.getDeviceSn()));
            }
        }
        if(device.getOnline()!=null&&saved.getOnline().equals(device.getOnline())){
            redisUtil.set(DeviceRedisKey.DEVICE_REDIS_STATUS +saved.getDeviceId(),device.getOnline(),5,TimeUnit.MINUTES);
        }
//        if (menu.getParentId() == null) {
//            menu.setParentId(0L);
//        }
//        if (menu.getPriority() == null) {
//            menu.setPriority(0);
//        }
        device.setUpdateTime(new Date());
        updateById(device);
        // 同步权限表里的信息
//        systemAuthorityService.saveOrUpdateAuthority(menu.getMenuId(), ResourceType.menu);
        return device;
    }

    /**
     * 检查设备编码是否存在
     *
     * @param deviceSn
     * @return
     */
    Boolean isExist(String deviceSn){
        QueryWrapper<IotDevice> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(IotDevice::getDeviceSn, deviceSn);
        int count = count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 修改菜单资源
     *
     * @param device
     * @return
     */
    @Override
    public IotDevice adjustUpdate(IotDevice device) {
        IotDevice saved = getById(device.getDeviceId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在!", device.getDeviceId()));
        }
        IotDevice iotDeviceUpdata = new IotDevice();
        iotDeviceUpdata.setDeviceId(device.getDeviceId());
        iotDeviceUpdata.setDistribuImgY(device.getDistribuImgY());
        iotDeviceUpdata.setDistribuImgX(device.getDistribuImgX());
        iotDeviceUpdata.setDistribuImgW(device.getDistribuImgW());
        iotDeviceUpdata.setDistribuImgH(device.getDistribuImgH());
        iotDeviceUpdata.setUpdateTime(new Date());
        updateById(iotDeviceUpdata);
        return device;
    }

    /**
     * 移除Action
     *
     * @param deviceId
     * @return
     */
    @Override
    public void remove(Long deviceId) {
//        IotDevice aciton = getById(deviceId);
//        if (aciton != null && aciton.getIsPersist().equals(SystemConstants.ENABLED)) {
//            throw new BaseException(String.format("默认数据,禁止删除"));
//        }
        removeById(deviceId);
    }

    @Override
    public void batchRemove(RequestParams requestParams) {
        IotDevice query = requestParams.mapToBean(IotDevice.class);
        QueryWrapper<IotDevice> queryWrapper = new QueryWrapper();
        Object deviceIds = requestParams.getRequestMap().get("deviceIds");
        List<String> deviceIdList = new ArrayList<>();

        if(deviceIds!=null){
            String deviceIdStr = String.valueOf(deviceIds);
            if(StringUtils.isNotBlank(deviceIdStr)){
                deviceIdList = Arrays.asList(deviceIdStr.split(","));
            }
        }
        queryWrapper.lambda()
                .in((deviceIdList!=null&&deviceIdList.size()>0),IotDevice::getDeviceId,deviceIdList)
                .eq(ObjectUtils.isNotEmpty(query.getOnline()), IotDevice::getOnline, query.getOnline())
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), IotDevice::getTenantId, query.getTenantId())
                .and(ObjectUtils.isNotEmpty(query.getDeviceName()) || ObjectUtils.isNotEmpty(query.getDeviceSn()),
                        wrapper ->
                                wrapper
                                        .like(ObjectUtils.isNotEmpty(query.getDeviceName()), IotDevice::getDeviceName, query.getDeviceName())
                                        .or()
                                        .like(ObjectUtils.isNotEmpty(query.getDeviceSn()), IotDevice::getDeviceSn, query.getDeviceSn())
                );

        List<IotDevice> iotDevices = baseMapper.selectList(queryWrapper);
        if(iotDevices!=null&&iotDevices.size()>0) {
            List<Long> collect = iotDevices.stream().map(m -> m.getDeviceId()).collect(Collectors.toList());
            baseMapper.deleteBatchIds(collect);
        }
    }

    @Override
    public void getDeviceInfoToCloud(DeviceInfoRes deviceInfoResBody) {
        if(deviceInfoResBody.getDeviceInfoResList()!=null&&deviceInfoResBody.getDeviceInfoResList().size()>0) {
            IotDeviceTask deviceTaskInfo = iotDeviceTaskService.getById(deviceInfoResBody.getTaskId());
            if (deviceTaskInfo != null) {
                deviceTaskInfo.setCode(deviceInfoResBody.getTaskCode());
                deviceTaskInfo.setStatus(deviceInfoResBody.getTaskStatus());
                deviceTaskInfo.setContent(JSON.toJSONString(deviceInfoResBody));
                iotDeviceTaskService.updateById(deviceTaskInfo);
            }
            for (DeviceInfoRes item : deviceInfoResBody.getDeviceInfoResList()) {
                IotDevice iotDevice = new IotDevice();
                iotDevice.setDeviceSn(item.getDeviceSn());
                iotDevice.setDeviceIp(item.getDeviceIp());
                iotDevice.setOnline(item.getOnlineStatus());
                iotDevice.setEthernet(item.getAlarmInfo().getEthernet());
                iotDevice.setModem(item.getAlarmInfo().getModem());
                iotDevice.setRam(item.getAlarmInfo().getRam());
                iotDevice.setData(item.getAlarmInfo().getData());
                iotDevice.setSimCard(item.getAlarmInfo().getSimCard());
                iotDevice.setSoftwareVersion(item.getSoftwareVersion());
                UpdateWrapper<IotDevice> updateWrapper = new UpdateWrapper();
                updateWrapper.lambda().eq(IotDevice::getDeviceSn,item.getDeviceSn());
                redisUtil.set(DeviceRedisKey.DEVICE_REDIS_STATUS +item.getDeviceSn(),item.getOnlineStatus(),5,TimeUnit.MINUTES);
                if(item.getOnlineStatus().equals(1)){
                    Object o = redisUtil.get(DeviceRedisKey.DEVICE_REDIS_SEND_STATUS + item.getDeviceSn());
                    if(o!=null){
                        redisUtil.del(DeviceRedisKey.DEVICE_REDIS_SEND_STATUS);
                    }
                }
                baseMapper.update(iotDevice,updateWrapper);
            }
        }
    }

    @Override
    public void getDeviceSoftToCloud(DeviceInfoRes deviceInfoRes) {
        IotDeviceTask deviceTaskInfo = iotDeviceTaskService.getById(deviceInfoRes.getTaskId());
        if (deviceTaskInfo != null) {
            deviceTaskInfo.setCode(deviceInfoRes.getTaskCode());
            deviceTaskInfo.setStatus(deviceInfoRes.getTaskStatus());
            deviceTaskInfo.setContent(JSON.toJSONString(deviceInfoRes));
            iotDeviceTaskService.updateById(deviceTaskInfo);
        }
        IotDevice iotDevice = new IotDevice();
        iotDevice.setDeviceSn(deviceInfoRes.getDeviceSn());
        if(deviceInfoRes.getTotalSize().equals(deviceInfoRes.getCompletedSize())){
            iotDevice.setUpgradeStatus(1);
            iotDevice.setUpgradeProgress(0d);
        }else {
            BigDecimal bigDecimal1 = new BigDecimal(deviceInfoRes.getCompletedSize());
            BigDecimal bigDecimal2 = new BigDecimal(deviceInfoRes.getTotalSize());
            if(bigDecimal2.compareTo(bigDecimal1)>0){
                iotDevice.setUpgradeStatus(0);
                BigDecimal bigDecimal = bigDecimal1.divide(bigDecimal2,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                iotDevice.setUpgradeProgress(bigDecimal.doubleValue());
            }else{
                iotDevice.setUpgradeStatus(1);
                iotDevice.setUpgradeProgress(0d);
            }
        }
        UpdateWrapper<IotDevice> updateWrapper = new UpdateWrapper();
        updateWrapper.lambda().eq(IotDevice::getDeviceSn,deviceInfoRes.getDeviceSn());
        baseMapper.update(iotDevice,updateWrapper);
    }

    @Override
    public Map<String,Object> getDeviceInfoDashboard() {
        Map<String,Object> resultMap = Maps.newHashMap();
        Map<String,Object> deviceOnline = baseMapper.selectDeviceOnline();
        Map<String, Object> deviceAlarm = baseMapper.selectDeviceAlarm();
        List<Map<String, Object>> iotDevicesList = baseMapper.selectDeviceDistList();
        resultMap.put("deviceOnline",deviceOnline);
        resultMap.put("deviceAlarm",deviceAlarm);
        resultMap.put("iotDevicesList",iotDevicesList);
        return resultMap;
    }

    @Override
    public Map<String, Object> getDeviceList() {
        Map<String,Object> deviceOnline = baseMapper.selectDeviceOnline();
        List<Map<String, Object>> maps1 = baseMapper.selectDeviceCateOnline();
        deviceOnline.put("onlineList",maps1);
        List<Map<String, Object>> maps2 = baseMapper.selectDeviceCateOffline();
        deviceOnline.put("offlineList",maps2);
        return deviceOnline;
    }
}
