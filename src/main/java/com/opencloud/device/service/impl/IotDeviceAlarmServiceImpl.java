package com.opencloud.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.entity.IotDeviceAlarm;
import com.opencloud.device.entity.IotDeviceTask;
import com.opencloud.device.form.req.AlarmInfoRes;
import com.opencloud.device.mapper.IotDeviceAlarmMapper;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.IotDeviceAlarmService;
import com.opencloud.device.service.IotDeviceService;
import com.opencloud.device.service.IotDeviceTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: liuyadu
 * @date: 2018/11/12 16:26
 * @description:
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class IotDeviceAlarmServiceImpl extends BaseServiceImpl<IotDeviceAlarmMapper, IotDeviceAlarm> implements IotDeviceAlarmService {

    @Autowired
    private IotDeviceTaskService iotDeviceTaskService;
    @Autowired
    private IotDeviceService iotDeviceService;
//    @Autowired
//    private RedisUtil redisUtil;
//
//    private final static String APP_PREFIX = "app:";
//    private final static String APP_API_KEY_PREFIX = "app_api_key:";
//    private final static String APP_CLIENT_PREFIX = "app_client:";
//
//    /**
//     * token有效期，默认12小时
//     */
//    public static final int ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 12;
//    /**
//     * token有效期，默认7天
//     */
//    public static final int REFRESH_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 7;
//
    /**
     * 查询应用列表
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<IotDeviceAlarm> findPage(PageParams pageParams) {
        IotDeviceAlarm query = pageParams.mapToBean(IotDeviceAlarm.class);
        QueryWrapper<IotDeviceAlarm> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getDeviceId()), IotDeviceAlarm::getDeviceId, query.getDeviceId())
                .eq(ObjectUtils.isNotEmpty(query.getAlarmType()), IotDeviceAlarm::getAlarmType, query.getAlarmType())
                .likeRight(ObjectUtils.isNotEmpty(query.getContent()), IotDeviceAlarm::getContent, query.getContent())
                .orderByDesc(IotDeviceAlarm::getCreateTime);
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    @Override
    public void getDeviceAlarmToCloud(AlarmInfoRes alarmInfoRes) {
        IotDeviceTask deviceTask = iotDeviceTaskService.getById(alarmInfoRes.getTaskId());
        if(deviceTask!=null){
            deviceTask.setCode(alarmInfoRes.getTaskCode());
            deviceTask.setStatus(alarmInfoRes.getTaskStatus());
            deviceTask.setContent(JSON.toJSONString(alarmInfoRes));
            iotDeviceTaskService.updateById(deviceTask);
            IotDevice iotDevice = new IotDevice();
            iotDevice.setDeviceId(deviceTask.getDeviceId());
            iotDevice.setEthernet(alarmInfoRes.getEthernet());
            iotDevice.setRam(alarmInfoRes.getRam());
            iotDevice.setData(alarmInfoRes.getData());
            iotDevice.setModem(alarmInfoRes.getModem());
            iotDevice.setSimCard(alarmInfoRes.getSimCard());
            iotDeviceService.updateById(iotDevice);
        }
    }
    //
//    /**
//     * 获取app详情
//     *
//     * @param appId
//     * @return
//     */
//    @Override
//    public IotDeviceAlarm get(String appId) {
//        String key = APP_PREFIX + appId;
//        if (redisUtil.hasKey(key)) {
//            return (IotDeviceAlarm) redisUtil.get(key);
//        }
//        IotDeviceAlarm app = getById(appId);
//        if (app != null) {
//            redisUtil.set(key, app, 2, TimeUnit.HOURS);
//        }
//        return app;
//    }
//
//    /**
//     * 根据ApiKey获取app信息
//     *
//     * @param apiKey
//     * @return
//     */
//    @Override
//    public IotDeviceAlarm getByApiKey(String apiKey) {
//        String key = APP_API_KEY_PREFIX + apiKey;
//        if (redisUtil.hasKey(key)) {
//            return (IotDeviceAlarm) redisUtil.get(key);
//        }
//        QueryWrapper<IotDeviceAlarm> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceAlarm::getApiKey, apiKey);
//        IotDeviceAlarm app = getOne(queryWrapper);
//        if (app != null) {
//            redisUtil.set(key, app, 2, TimeUnit.HOURS);
//        }
//        return app;
//    }
//
//    /**
//     * 获取app和应用信息
//     *
//     * @return
//     */
//    @Override
//    public BaseClientInfo getByClientId(String clientId) {
//        BaseClientDetails baseClientDetails = null;
//        try {
//            baseClientDetails = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(clientId);
//        } catch (exception e) {
//            return null;
//        }
//        String appId = baseClientDetails.getAdditionalInformation().get("appId").toString();
////        String key = APP_CLIENT_PREFIX + appId + ":" + baseClientDetails.getClientId();
////        if (redisUtil.hasKey(key)) {
////            return (BaseClientInfo) redisUtil.get(key);
////        }
//        BaseClientInfo baseClientInfo = new BaseClientInfo();
//        BeanUtils.copyProperties(baseClientDetails, baseClientInfo);
////        baseClientInfo.setAuthorities(systemAuthorityService.findAuthorityByApp(appId));
////        redisUtil.set(key, baseClientInfo, 2, TimeUnit.HOURS);
//        return baseClientInfo;
//    }
//
//    /**
//     * 更新应用开发新型
//     *
//     * @param client
//     */
//    @Override
//    public void updateClientInfo(BaseClientInfo client) {
//        BaseClientInfo baseClientInfo = getByClientId(client.getClientId());
//        if (baseClientInfo == null) {
//            return;
//        }
//        String appId = baseClientInfo.getAdditionalInformation().get("appId").toString();
//        String key = APP_CLIENT_PREFIX + appId + ":" + baseClientInfo.getClientId();
//        jdbcClientDetailsService.updateClientDetails(client);
//        redisUtil.del(key);
//    }
//
//    /**
//     * 添加应用
//     *
//     * @param app
//     * @return 应用信息
//     */
//    @Override
//    public IotDeviceAlarm add(IotDeviceAlarm app) {
//        String appId = String.valueOf(System.currentTimeMillis());
//        String apiKey = RandomUtil.randomString(24);
//        String secretKey = RandomUtil.randomString(32);
//        app.setAppId(appId);
//        app.setApiKey(apiKey);
//        app.setSecretKey(secretKey);
//        app.setCreateTime(new Date());
//        app.setUpdateTime(app.getCreateTime());
//        if (app.getIsPersist() == null) {
//            app.setIsPersist(0);
//        }
//        save(app);
//        Map info = BeanUtil.beanToMap(app);
//        // 功能授权
//        BaseClientInfo client = new BaseClientInfo();
//        client.setClientId(app.getApiKey());
//        client.setClientSecret(app.getSecretKey());
//        client.setAdditionalInformation(info);
//        Set<String> resourceIds = new HashSet();
//        resourceIds.add("default-oauth2-resource");
//        // 设置默认可访问资源服务器Id,如果没设置，就是对所有的resource都有访问权限
//        client.setResourceIds(resourceIds);
//        client.setAuthorizedGrantTypes(Arrays.asList("authorization_code", "client_credentials", "implicit", "refresh_token"));
//        client.setAccessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS);
//        client.setRefreshTokenValiditySeconds(REFRESH_TOKEN_VALIDITY_SECONDS);
//        jdbcClientDetailsService.addClientDetails(client);
//        return app;
//    }
//
//    /**
//     * 修改应用
//     *
//     * @param systemApp 应用
//     * @return 应用信息
//     */
//    @Override
//    public IotDeviceAlarm update(IotDeviceAlarm systemApp) {
//        // 修改客户端附加信息
//        IotDeviceAlarm app = get(systemApp.getAppId());
//        String apiKey = app.getApiKey();
//        String secretKey = app.getSecretKey();
//        BeanUtils.copyProperties(systemApp, app);
//        app.setApiKey(apiKey);
//        app.setSecretKey(secretKey);
//        updateById(app);
//        BaseClientDetails client = (BaseClientDetails) jdbcClientDetailsService.loadClientByClientId(app.getApiKey());
//        client.setAdditionalInformation(BeanUtil.beanToMap(app));
//        jdbcClientDetailsService.updateClientDetails(client);
//        redisUtil.del(APP_PREFIX + app.getAppId());
//        redisUtil.del(APP_API_KEY_PREFIX + app.getApiKey());
//        redisUtil.del(APP_CLIENT_PREFIX + app.getAppId() + ":" + app.getApiKey());
//        return app;
//    }
//
//    /**
//     * 重置秘钥
//     *
//     * @param appId
//     * @return
//     */
//    @Override
//    public String restSecret(String appId) {
//        IotDeviceAlarm app = get(appId);
//        if (app == null) {
//            throw new BaseException(appId + "应用不存在!");
//        }
//        if (app.getIsPersist().equals(SystemConstants.ENABLED)) {
//            throw new BaseException(String.format("默认数据,禁止修改"));
//        }
//        // 生成新的密钥
//        String secretKey = RandomUtil.randomString(32);
//        app.setSecretKey(secretKey);
//        updateById(app);
//        jdbcClientDetailsService.updateClientSecret(app.getApiKey(), secretKey);
//        redisUtil.del(APP_PREFIX + app.getAppId());
//        redisUtil.del(APP_API_KEY_PREFIX + app.getApiKey());
//        redisUtil.del(APP_CLIENT_PREFIX + app.getAppId() + ":" + app.getApiKey());
//        return secretKey;
//    }
//
//    @Override
//    public void remove(String appId) {
//        IotDeviceAlarm app = get(appId);
//        if (app == null) {
//            throw new BaseException(appId + "应用不存在!");
//        }
//        if (app.getIsPersist().equals(SystemConstants.ENABLED)) {
//            throw new BaseException(String.format("默认数据,禁止删除"));
//        }
//        // 移除应用权限
////        systemAuthorityService.removeAuthorityApp(appId);
//        removeById(app.getAppId());
//        jdbcClientDetailsService.removeClientDetails(app.getApiKey());
//        redisUtil.del(APP_PREFIX + app.getAppId());
//        redisUtil.del(APP_API_KEY_PREFIX + app.getApiKey());
//        redisUtil.del(APP_CLIENT_PREFIX + app.getAppId() + ":" + app.getApiKey());
//    }


}
