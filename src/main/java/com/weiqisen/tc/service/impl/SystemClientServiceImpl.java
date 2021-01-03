package com.weiqisen.tc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.weiqisen.tc.entity.SystemClient;
import com.weiqisen.tc.entity.SystemClientApi;
import com.weiqisen.tc.exception.BaseException;
import com.weiqisen.tc.mapper.SystemClientApiMapper;
import com.weiqisen.tc.mapper.SystemClientMapper;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.service.SystemClientService;
import com.weiqisen.tc.utils.ConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemClientServiceImpl extends BaseServiceImpl<SystemClientMapper, SystemClient> implements SystemClientService {

    @Autowired
    private SystemClientApiMapper systemClientApiMapper;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemClient> findPage(PageParams pageParams) {
        SystemClient query = pageParams.mapToBean(SystemClient.class);
        QueryWrapper<SystemClient> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getClientName()), SystemClient::getClientName, query.getClientName())
                .eq(ObjectUtils.isNotEmpty(query.getClientAppid()), SystemClient::getClientAppid, query.getClientAppid())
                .orderByDesc(SystemClient::getCreateTime);
        return baseMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }

    @Override
    public SystemClient find(String appId) {
        QueryWrapper<SystemClient> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(appId), SystemClient::getClientAppid, appId);
        return baseMapper.selectOne(queryWrapper);
    }

//    /**
//     * 检查Action编码是否存在
//     *
//     * @param acitonCode
//     * @return
//     */
//    @Override
//    public Boolean isExist(String acitonCode) {
//        QueryWrapper<SystemClient> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda()
//                .eq(SystemClient::getActionCode, acitonCode);
//        int count = count(queryWrapper);
//        return count > 0 ? true : false;
//    }

    /**
     * 添加Action操作
     *
     * @param action
     * @return
     */
    @Override
    public SystemClient add(SystemClient action) {
//        if (isExist(aciton.getActionCode())) {
//            throw new BaseException(String.format("%s编码已存在!", aciton.getActionCode()));
//        }
        action.setCreateTime(new Date());
        action.setUpdateTime(action.getCreateTime());
        action.setClientAppid(UUID.randomUUID().toString().replaceAll("-", ""));
        action.setClientCode(ConversionUtils.Md5CodeEncode(action.getClientAppid()));
        save(action);
        // 同步权限表里的信息
//        systemAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
        return action;
    }

    /**
     * 修改Action操作
     *
     * @param aciton
     * @return
     */
    @Override
    public SystemClient update(SystemClient aciton) {
        SystemClient saved = getById(aciton.getClientId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在", aciton.getClientId()));
        }
//        if (!saved.getActionCode().equals(aciton.getActionCode())) {
//            // 和原来不一致重新检查唯一性
//            if (isExist(aciton.getActionCode())) {
//                throw new BaseException(String.format("%s编码已存在!", aciton.getActionCode()));
//            }
//        }
        aciton.setUpdateTime(new Date());
        updateById(aciton);
        return aciton;
    }

    /**
     * 移除Action
     *
     * @param actionId
     * @return
     */
    @Override
    public void remove(Long actionId) {
//        SystemClient aciton = getById(actionId);
        removeById(actionId);
    }

    /**
     * 添加功能按钮权限
     *
     * @param clientId
     * @param apiIds
     * @return
     */
    @Override
    public void addClientApi(Long clientId, String... apiIds) {
        if (clientId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeAuthorityClient(clientId);
        if (apiIds != null && apiIds.length > 0) {
            for (String id : apiIds) {
                Long apiId = Long.parseLong(id);
                SystemClientApi systemClientApi = new SystemClientApi();
                systemClientApi.setClientId(clientId);
                systemClientApi.setApiId(apiId);
                systemClientApi.setCreateTime(new Date());
                systemClientApi.setUpdateTime(systemClientApi.getCreateTime());
                systemClientApiMapper.insert(systemClientApi);
            }
        }
    }

    /**
     * 移除功能按钮权限
     *
     * @param clientId
     */
    @Override
    public void removeAuthorityClient(Long clientId) {
        baseMapper.deleteClientApi(clientId);
    }
}
