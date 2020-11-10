package com.opencloud.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.opencloud.device.entity.SystemAction;
import com.opencloud.device.entity.SystemActionApi;
import com.opencloud.device.entity.SystemRole;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.mapper.SystemActionApiMapper;
import com.opencloud.device.mapper.SystemActionMapper;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.SystemConstants;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.security.BaseAuthority;
import com.opencloud.device.service.SystemActionService;
import com.opencloud.device.service.SystemRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemActionServiceImpl extends BaseServiceImpl<SystemActionMapper, SystemAction> implements SystemActionService {
    @Autowired
    private SystemRoleService systemRoleService;

    @Autowired
    private SystemActionApiMapper systemActionApiMapper;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemAction> findPage(PageParams pageParams) {
        SystemAction query = pageParams.mapToBean(SystemAction.class);
        QueryWrapper<SystemAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getActionCode()), SystemAction::getActionCode, query.getActionCode())
                .likeRight(ObjectUtils.isNotEmpty(query.getActionName()), SystemAction::getActionName, query.getActionName());
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }

    /**
     * 查询菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @Override
    public List<SystemAction> findListByMenuId(Long menuId) {
        QueryWrapper<SystemAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SystemAction::getMenuId, menuId);
        List<SystemAction> list = list(queryWrapper);
        //根据优先级从大到小排序
        list.sort((SystemAction h1, SystemAction h2) -> h1.getPriority().compareTo(h2.getPriority()));
        return list;
    }


    /**
     * 检查Action编码是否存在
     *
     * @param acitonCode
     * @return
     */
    @Override
    public Boolean isExist(String acitonCode) {
        QueryWrapper<SystemAction> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SystemAction::getActionCode, acitonCode);
        int count = count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加Action操作
     *
     * @param aciton
     * @return
     */
    @Override
    public SystemAction add(SystemAction aciton) {
        if (isExist(aciton.getActionCode())) {
            throw new BaseException(String.format("%s编码已存在!", aciton.getActionCode()));
        }
        if (aciton.getMenuId() == null) {
            aciton.setMenuId(0L);
        }
        if (aciton.getPriority() == null) {
            aciton.setPriority(0);
        }
        if (aciton.getStatus() == null) {
            aciton.setStatus(SystemConstants.ENABLED);
        }
        if (aciton.getIsPersist() == null) {
            aciton.setIsPersist(SystemConstants.DISABLED);
        }
        aciton.setCreateTime(new Date());
        aciton.setUpdateTime(aciton.getCreateTime());
        save(aciton);
        // 同步权限表里的信息
//        systemAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
        return aciton;
    }

    /**
     * 修改Action操作
     *
     * @param aciton
     * @return
     */
    @Override
    public SystemAction update(SystemAction aciton) {
        SystemAction saved = getById(aciton.getActionId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在", aciton.getActionId()));
        }
        if (!saved.getActionCode().equals(aciton.getActionCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(aciton.getActionCode())) {
                throw new BaseException(String.format("%s编码已存在!", aciton.getActionCode()));
            }
        }
        if (aciton.getMenuId() == null) {
            aciton.setMenuId(0L);
        }
        if (aciton.getPriority() == null) {
            aciton.setPriority(0);
        }
        aciton.setUpdateTime(new Date());
        updateById(aciton);
        // 同步权限表里的信息
//        systemAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
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
        SystemAction aciton = getById(actionId);
        if (aciton != null && aciton.getIsPersist().equals(SystemConstants.ENABLED)) {
            throw new BaseException(String.format("默认数据,禁止删除"));
        }
//        systemAuthorityService.removeAuthorityAction(actionId);
//        systemAuthorityService.removeAuthority(actionId, ResourceType.action);
        removeById(actionId);
    }

    /**
     * 移除菜单相关资源
     *
     * @param menuId
     */
    @Override
    public void removeByMenuId(Long menuId) {
        List<SystemAction> actionList = findListByMenuId(menuId);
        if (actionList != null && actionList.size() > 0) {
            for (SystemAction action : actionList) {
                remove(action.getActionId());
            }
        }
    }

    @Override
    public List<BaseAuthority> findActionByUser(Long userId, boolean equals, String type) {
        List<BaseAuthority> authorities = Lists.newArrayList();
        List<SystemRole> rolesList = systemRoleService.findRolesByUserId(userId);
        if (rolesList != null) {
            for (SystemRole role : rolesList) {
                // 加入角色已授权
                List<BaseAuthority> roleGrantedAuthority = systemRoleService.findActionByRole(role.getRoleId(), type);
//                findAuthorityByRole(role.getRoleId());
                if (roleGrantedAuthority != null && roleGrantedAuthority.size() > 0) {
                    authorities.addAll(roleGrantedAuthority);
                }
            }
        }
        // 加入用户特殊授权
        List<BaseAuthority> userGrantedAuthority = baseMapper.selectAuthorityByUser(userId, type);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        // 权限去重
        HashSet h = new HashSet(authorities);
        authorities.clear();
        authorities.addAll(h);
        return authorities;
    }

    /**
     * 添加功能按钮权限
     *
     * @param actionId
     * @param apiIds
     * @return
     */
    @Override
    public void addActionApi(Long actionId, String... apiIds) {
        if (actionId == null) {
            return;
        }
        // 移除操作已绑定接口
        removeAuthorityAction(actionId);
        if (apiIds != null && apiIds.length > 0) {
            for (String id : apiIds) {
                Long apiId = Long.parseLong(id);
                SystemActionApi systemActionApi = new SystemActionApi();
                systemActionApi.setActionId(actionId);
                systemActionApi.setApiId(apiId);
                systemActionApi.setCreateTime(new Date());
                systemActionApi.setUpdateTime(systemActionApi.getCreateTime());
                systemActionApiMapper.insert(systemActionApi);
            }
        }
    }

    /**
     * 移除功能按钮权限
     *
     * @param actionId
     */
    @Override
    public void removeAuthorityAction(Long actionId) {
        baseMapper.deleteActionApi(actionId);
    }
}
