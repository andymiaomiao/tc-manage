package com.weiqisen.tc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.weiqisen.tc.entity.SystemActionApi;
import com.weiqisen.tc.entity.SystemApi;
import com.weiqisen.tc.exception.BaseException;
import com.weiqisen.tc.form.req.SystemApiBatchUpdateReq;
import com.weiqisen.tc.mapper.SystemActionApiMapper;
import com.weiqisen.tc.mapper.SystemApiMapper;
import com.weiqisen.tc.model.CommonConstants;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.SystemConstants;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.service.SystemApiService;
import com.weiqisen.tc.utils.ConversionUtils;
import com.weiqisen.tc.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemApiServiceImpl extends BaseServiceImpl<SystemApiMapper, SystemApi> implements SystemApiService {
    @Autowired
    private SystemActionApiMapper systemActionApiMapper;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemApi> findPage(PageParams pageParams) {
        SystemApi query = pageParams.mapToBean(SystemApi.class);
        QueryWrapper<SystemApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getApiType()), SystemApi::getApiType, query.getApiType())
                .eq(ObjectUtils.isNotEmpty(query.getStatus()), SystemApi::getStatus, query.getStatus())
                .eq(ObjectUtils.isNotEmpty(query.getIsAuth()), SystemApi::getIsAuth, query.getIsAuth())
                .and(
                        ObjectUtils.isNotEmpty(query.getPath()) || ObjectUtils.isNotEmpty(query.getApiName()) || ObjectUtils.isNotEmpty(query.getApiCode()),
                        wrapper ->
                                wrapper
                                        .likeRight(ObjectUtils.isNotEmpty(query.getPath()), SystemApi::getPath, query.getPath())
                                        .or()
                                        .likeRight(ObjectUtils.isNotEmpty(query.getApiName()), SystemApi::getApiName, query.getApiName())
                                        .or()
                                        .likeRight(ObjectUtils.isNotEmpty(query.getApiCode()), SystemApi::getApiCode, query.getApiCode())
                );
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SystemApi> findList(String serviceId) {
        QueryWrapper<SystemApi> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(ObjectUtils.isNotEmpty(serviceId), SystemApi::getServiceId, serviceId);
        List<SystemApi> list = list(queryWrapper);
        return list;
    }

    @Override
    public Map findActionList(String actionId, Integer actionType) {
        Map map = Maps.newHashMap();
        map.put("actionId", actionId);
        map.put("actionType", actionType);
        map.put("status", 1);
        List<SystemApi> apiList = baseMapper.findApiList(map);
        List<SystemApi> actionList = baseMapper.findActionList(map);
        Map resultMap = Maps.newHashMap();
        resultMap.put("apiList", apiList);
        resultMap.put("actionList", actionList);
        return resultMap;
    }

    @Override
    public Map findClientList(String clientId, Integer actionType) {
        Map map = Maps.newHashMap();
        map.put("clientId", clientId);
        map.put("actionType", actionType);
        map.put("status", 1);
        List<SystemApi> apiList = baseMapper.findApiList(map);
        List<SystemApi> clientList = baseMapper.findClientList(map);
        Map resultMap = Maps.newHashMap();
        resultMap.put("apiList", apiList);
        resultMap.put("clientList", clientList);
        return resultMap;
    }

    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    @Override
    public Boolean isExist(String apiCode) {
        QueryWrapper<SystemApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemApi::getApiCode, apiCode);
        int count = count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    @Override
    public void add(SystemApi api) {
        if (StringUtils.isBlank(api.getApiCode())) {
            api.setApiCode(ConversionUtils.Md5CodeEncode(api.getApiType() + api.getPath()));
        }
        if (isExist(api.getApiCode())) {
            throw new BaseException(String.format("%s编码已存在!", api.getApiCode()));
        }
        if (api.getPriority() == null) {
            api.setPriority(0);
        }
        if (api.getStatus() == null) {
            api.setStatus(SystemConstants.ENABLED);
        }
        if (api.getIsPersist() == null) {
            api.setIsPersist(0);
        }
        if (api.getIsAuth() == null) {
            api.setIsAuth(1);
        }
        save(api);
    }

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    @Override
    public void update(SystemApi api) {
        SystemApi saved = getById(api.getApiId());
        if (saved == null) {
            throw new BaseException("信息不存在!");
        }
        if (!saved.getApiCode().equals(api.getApiCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(api.getApiCode())) {
                throw new BaseException(String.format("%s编码已存在!", api.getApiCode()));
            }
        }
        api.setUpdateTime(new Date());
        updateById(api);
    }

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    @Override
    public SystemApi getByCode(String apiCode) {
        QueryWrapper<SystemApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemApi::getApiCode, apiCode);
        return getOne(queryWrapper);
    }


    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    @Override
    public void remove(Long apiId) {
        SystemApi api = getById(apiId);
        if (api != null && api.getIsPersist().equals(SystemConstants.ENABLED)) {
            throw new BaseException(String.format("默认数据,禁止删除"));
        }
        removeById(apiId);
    }

    /**
     * 功能按钮绑定API
     *
     * @param actionId
     * @param apiIds
     * @return
     */
    @Override
    public void addApiAction(Long actionId, String... apiIds) {
        if (actionId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeApiAction(actionId);
        if (apiIds != null && apiIds.length > 0) {
            for (String id : apiIds) {
                Long apiId = Long.parseLong(id);
                SystemActionApi authority = new SystemActionApi();
                authority.setActionId(actionId);
                authority.setApiId(apiId);
                systemActionApiMapper.insert(authority);
            }
        }
    }

    /**
     * //     * 移除功能按钮权限
     * //     *
     * //     * @param actionId
     * //
     */
//    @Override
    public void removeApiAction(Long actionId) {
        QueryWrapper<SystemActionApi> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SystemActionApi::getActionId, actionId);

        Map map = Maps.newHashMap();
        map.put("actionId", actionId);
        baseMapper.deleteApiAction(map);
    }

    /**
     * 获取等待更新的服务器列表
     */
    @Override
    public List<Map> getBatchUpdateServiceList() {
        List<Map> list = Lists.newArrayList();
        Set<String> keys = redisUtil.hkeys(CommonConstants.API_RESOURCE);
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String serviceId = iterator.next();
            Map<String, Object> map = (Map<String, Object>) redisUtil.hget(CommonConstants.API_RESOURCE, serviceId);
            map.remove("list");
            list.add(map);
        }
        return list;
    }


    @Override
    public void batchUpdateServiceApi(SystemApiBatchUpdateReq systemApiBatchUpdateReq) {
        baseMapper.batchUpdateServiceApi(systemApiBatchUpdateReq.getSystemApiList());
    }

    /**
     * 更新资源服务器
     *
     * @param serviceIds
     * @return
     */
    @Override
    public boolean batchUpdateService(String... serviceIds) {
        if (serviceIds != null && serviceIds.length > 0) {
            for (String serviceId : serviceIds) {
                if (redisUtil.hHasKey(CommonConstants.API_RESOURCE, serviceId)) {
                    List<String> codes = Lists.newArrayList();
                    Map<String, Object> map = (Map<String, Object>) redisUtil.hget(CommonConstants.API_RESOURCE, serviceId);
                    List<Map<String, String>> list = (List<Map<String, String>>) map.get("list");
                    Iterator iterator = list.iterator();
                    while (iterator.hasNext()) {
                        Map obj = (Map) iterator.next();
                        try {
                            SystemApi api = BeanUtil.mapToBean(obj, SystemApi.class, true);
                            codes.add(api.getApiCode());
                            SystemApi save = getByCode(api.getApiCode());
                            if (save == null) {
                                api.setIsPersist(1);
                                add(api);
                            } else {
                                api.setApiId(save.getApiId());
                                update(api);
                            }
                        } catch (Exception e) {
                            log.error("添加资源error:", e);
                        }
                    }
                    if (codes != null && codes.size() > 0) {
                        // 清理无效权限数据
//                        systemAuthorityService.clearInvalidApi(serviceId, codes);
                    }
                }
                redisUtil.hdel(CommonConstants.API_RESOURCE, serviceId);
            }
            return true;
        }
        return false;
    }

    @Override
    public Map findMenuApiList(Long menuId,Integer apiType) {
//        Map map = Maps.newHashMap();
//        map.put("actionId", actionId);
//        map.put("actionType", actionType);
//        map.put("status", 1);
//        List<SystemApi> apiList = baseMapper.findApiList(map);
//        List<SystemApi> actionList = baseMapper.findActionList(map);
//        Map resultMap = Maps.newHashMap();
//        resultMap.put("apiList", apiList);
//        resultMap.put("actionList", actionList);
//        return resultMap;


        Map map = Maps.newHashMap();
//        map.put("apiType",apiType);
        map.put("status", 1);

        Map<String, Object> resultMap = Maps.newHashMap();
        List<SystemApi> menuAllList = baseMapper.findMenuAllList(map);
        map.put("menuId",menuId);
        List<SystemApi> menuGrantList = baseMapper.findMenuGrantList(map);
        List<Long> menuList = new ArrayList<>();
        if(menuGrantList!=null&&menuGrantList.size()>0) {
            menuList = menuGrantList.stream().map(m->m.getApiId()).collect(Collectors.toList());
        }
        resultMap.put("menuAllList", menuAllList);
        resultMap.put("menuGrantList", menuList);
        return resultMap;

//        return authorities;

    }
}
