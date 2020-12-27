package com.weiqisen.tc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Maps;
import com.weiqisen.tc.entity.SystemTenant;
import com.weiqisen.tc.entity.SystemTenantAction;
import com.weiqisen.tc.entity.SystemTenantMenu;
import com.weiqisen.tc.exception.BaseException;
import com.weiqisen.tc.form.res.TenantMenuAllRes;
import com.weiqisen.tc.mapper.SystemTenantActionMapper;
import com.weiqisen.tc.mapper.SystemTenantMapper;
import com.weiqisen.tc.mapper.SystemTenantMenuMapper;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.SystemConstants;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.service.SystemTenantService;
import com.weiqisen.tc.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author weiqisen
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemTenantServiceImpl extends BaseServiceImpl<SystemTenantMapper, SystemTenant> implements SystemTenantService {
    @Autowired
    private SystemTenantActionMapper systemTenantActionMapper;
    @Autowired
    private SystemTenantMenuMapper systemTenantMenuMapper;



    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemTenant> findPage(PageParams pageParams) {
        SystemTenant query = pageParams.mapToBean(SystemTenant.class);
        QueryWrapper<SystemTenant> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getMobile()), SystemTenant::getMobile, query.getMobile())
                .eq(ObjectUtils.isNotEmpty(query.getTenantType()), SystemTenant::getTenantType, query.getTenantType())
                .likeRight(ObjectUtils.isNotEmpty(query.getTenantName()), SystemTenant::getTenantName, query.getTenantName());
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SystemTenant> findList() {
        List<SystemTenant> list = list(new QueryWrapper<>());
        return list;
    }

    /**
     * 添加角色
     *
     * @param tenant 角色
     * @return
     */
    @Override
    public SystemTenant add(SystemTenant tenant) {
        if (isExist(tenant.getMobile())) {
            throw new BaseException(String.format("%s手机号已存在!", tenant.getMobile()));
        }
        if (tenant.getStatus() == null) {
            tenant.setStatus(SystemConstants.ENABLED);
        }
        tenant.setCreateTime(new Date());
        tenant.setUpdateTime(tenant.getCreateTime());
        save(tenant);
        return tenant;
    }

    /**
     * 更新角色
     *
     * @param tenant 角色
     * @return
     */
    @Override
    public SystemTenant update(SystemTenant tenant) {
        SystemTenant saved = getById(tenant.getTenantId());
        if (tenant == null) {
            throw new BaseException("信息不存在!");
        }
        if (!saved.getMobile().equals(tenant.getMobile())) {
            // 和原来不一致重新检查唯一性
            if (isExist(tenant.getMobile())) {
                throw new BaseException(String.format("%s手机号已存在!", tenant.getMobile()));
            }
        }
        tenant.setUpdateTime(new Date());
        updateById(tenant);
        return tenant;
    }

    /**
     * 删除角色
     *
     * @param tenantId 角色ID
     * @return
     */
    @Override
    public void remove(Long tenantId) {
        if (tenantId == null) {
            return;
        }
//        int count = getCountByRoleId(tenantId);
//        if (count > 0) {
//            throw new BaseException("该角色下存在授权人员,禁止删除!");
//        }
        removeById(tenantId);
    }

    @Override
    public Map<String, Object> authorityTenantMenu(Integer tenantType, Long tenantId) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<TenantMenuAllRes> tenantMenuAllRes = baseMapper.selectTenantMenuAllList(tenantId, tenantType);
        List<TenantMenuAllRes> tenantMenuGrantList = baseMapper.selectTenantMenuGrantList(tenantId, tenantType);
        List<Long> tenantMenuLeafList = new ArrayList<>();
        List<Long> tenantMenuHalfList = new ArrayList<>();
        if(tenantMenuGrantList!=null&&tenantMenuGrantList.size()>0) {
            tenantMenuHalfList = tenantMenuGrantList.stream().map(m->m.getMenuId()).collect(Collectors.toList());
            tenantMenuTran(tenantMenuGrantList, new ArrayList<>(), tenantMenuLeafList, 0L);
        }
        resultMap.put("roleMenuAllList", tenantMenuAllRes);
        resultMap.put("roleMenuGrantLeafList", tenantMenuLeafList);
        resultMap.put("roleMenuGrantHalfList", tenantMenuHalfList);
        return resultMap;
    }

    private void tenantMenuTran(List<TenantMenuAllRes> tenantMenuGrantList,List<Long> tenantMenuList,List<Long> collMenuList,Long parentId){
        List<TenantMenuAllRes> parentList = new ArrayList<>();
        List<TenantMenuAllRes> childList = new ArrayList<>();
        for (TenantMenuAllRes f : tenantMenuGrantList) {
            if (f.getParentId().equals(parentId)) {
                parentList.add(f);
                tenantMenuList.add(f.getMenuId());
            }else{
                childList.add(f);
            }
        }
        if(parentList!=null&&parentList.size()>0){
            for (TenantMenuAllRes parent : parentList) {
                tenantMenuList = new ArrayList<>();
                tenantMenuTran(childList,tenantMenuList,collMenuList ,parent.getMenuId());
                if (tenantMenuList!=null && tenantMenuList.size() > 0) {
                    continue;
                } else {
                    collMenuList.add(parent.getMenuId());
                }
            }
        }
    }

    @Override
    public void grantTenantMenu(Long tenantId, String... menuIds) {
        if (tenantId == null) {
            return;
        }
        SystemTenant tenant = baseMapper.selectById(tenantId);
        if (tenant == null) {
            return;
        }
        deleteGrantTenantMenu(tenantId);
        batchGrantTenantMenu(tenantId, Arrays.asList(menuIds));
    }

    private void deleteGrantTenantMenu(Long tenantId){
        QueryWrapper<SystemTenantAction> actionQueryWrapper = new QueryWrapper();
        actionQueryWrapper.lambda().eq(SystemTenantAction::getTenantId, tenantId);
        systemTenantActionMapper.delete(actionQueryWrapper);
        QueryWrapper<SystemTenantMenu> menuQueryWrapper = new QueryWrapper();
        menuQueryWrapper.lambda().eq(SystemTenantMenu::getTenantId, tenantId);
        systemTenantMenuMapper.delete(menuQueryWrapper);
    }

    private void batchGrantTenantMenu(Long tenantId,List<String> menuIds){
        systemTenantActionMapper.batchGrantTenantAction(tenantId,menuIds);
        systemTenantMenuMapper.batchGrantTenantMenu(tenantId,menuIds);
    }
    /**
     * 检测角色编码是否存在
     *
     * @param tenantMobile
     * @return
     */
    @Override
    public Boolean isExist(String tenantMobile) {
        if (StringUtil.isBlank(tenantMobile)) {
            throw new BaseException("tenantCode不能为空!");
        }
        QueryWrapper<SystemTenant> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemTenant::getMobile, tenantMobile);
        return count(queryWrapper) > 0;
    }

//    /**
//     * 用户添加角色
//     *
//     * @param userId
//     * @param tenants
//     * @return
//     */
//    @Override
//    public void saveRoles(Long userId, String... tenants) {
//        if (userId == null || tenants == null) {
//            return;
//        }
//        SystemUser user = systemUserService.getById(userId);
//        if (user == null) {
//            return;
//        }
//        if (CommonConstants.ROOT.equals(user.getUserName())) {
//            throw new BaseException("默认用户无需分配!");
//        }
//        // 先清空,在添加
//        removeRolesByUserId(userId);
//        if (tenants != null && tenants.length > 0) {
//            for (String tenantId : tenants) {
//                SystemTenantUser tenantUser = new SystemTenantUser();
//                tenantUser.setUserId(userId);
//                tenantUser.setRoleId(Long.parseLong(tenantId));
//                systemRoleUserMapper.insert(tenantUser);
//            }
//            // 批量保存
//        }
//    }

    /**
     * 角色添加成员
     *
     * @param tenantId
     * @param userIds
     */
    @Override
    public void saveUsers(Long tenantId, String... userIds) {
        if (tenantId == null || userIds == null) {
            return;
        }
//        // 先清空,在添加
//        removeUsersByRoleId(tenantId);
//        if (userIds != null && userIds.length > 0) {
//            for (String userId : userIds) {
//                SystemTenantUser tenantUser = new SystemTenantUser();
//                tenantUser.setUserId(Long.parseLong(userId));
//                tenantUser.setRoleId(tenantId);
//                systemRoleUserMapper.insert(tenantUser);
//            }
//            // 批量保存
//        }
    }

//    /**
//     * 查询角色成员
//     *
//     * @return
//     */
//    @Override
//    public List<SystemTenantUser> findUsersByRoleId(Long tenantId) {
//        QueryWrapper<SystemTenantUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SystemTenantUser::getRoleId, tenantId);
//        return systemRoleUserMapper.selectList(queryWrapper);
//    }

//    /**
//     * 获取角色所有授权组员数量
//     *
//     * @param tenantId
//     * @return
//     */
//    @Override
//    public int getCountByRoleId(Long tenantId) {
//        QueryWrapper<SystemTenantUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SystemTenantUser::getRoleId, tenantId);
//        int result = systemRoleUserMapper.selectCount(queryWrapper);
//        return result;
//    }

//    /**
//     * 获取组员角色数量
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public int getCountByUserId(Long userId) {
//        QueryWrapper<SystemTenantUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SystemTenantUser::getUserId, userId);
//        int result = systemRoleUserMapper.selectCount(queryWrapper);
//        return result;
//    }

//    /**
//     * 移除角色所有组员
//     *
//     * @param tenantId
//     * @return
//     */
//    @Override
//    public void removeUsersByRoleId(Long tenantId) {
//        QueryWrapper<SystemTenantUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SystemTenantUser::getRoleId, tenantId);
//        systemRoleUserMapper.delete(queryWrapper);
//    }

//    /**
//     * 移除组员的所有角色
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public void removeRolesByUserId(Long userId) {
//        QueryWrapper<SystemTenantUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SystemTenantUser::getUserId, userId);
//        systemRoleUserMapper.delete(queryWrapper);
//    }

//    /**
//     * 检测是否存在
//     *
//     * @param userId
//     * @param tenantId
//     * @return
//     */
//    @Override
//    public Boolean isExist(Long userId, Long tenantId) {
//        QueryWrapper<SystemTenantUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(SystemTenantUser::getRoleId, tenantId);
//        queryWrapper.lambda().eq(SystemTenantUser::getUserId, userId);
//        systemRoleUserMapper.delete(queryWrapper);
//        int result = systemRoleUserMapper.selectCount(queryWrapper);
//        return result > 0;
//    }


//    /**
//     * 获取组员角色
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public List<SystemTenant> findRolesByUserId(Long userId) {
//        List<SystemTenant> tenants = systemRoleUserMapper.selectUsersByRoleId(userId);
//        return tenants;
//    }

//    /**
//     * 获取用户角色ID列表
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public List<Long> findRoleIdsByUserId(Long userId) {
//        return systemRoleUserMapper.selectUsersIdByRoleId(userId);
//    }


}
