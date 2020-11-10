package com.opencloud.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Maps;
import com.opencloud.device.entity.*;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.form.res.TenantMenuAllRes;
import com.opencloud.device.mapper.SystemRoleActionMapper;
import com.opencloud.device.mapper.SystemRoleMapper;
import com.opencloud.device.mapper.SystemRoleMenuMapper;
import com.opencloud.device.mapper.SystemRoleUserMapper;
import com.opencloud.device.model.CommonConstants;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.model.SystemConstants;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.security.BaseAuthority;
import com.opencloud.device.service.SystemRoleService;
import com.opencloud.device.service.SystemUserService;
import com.opencloud.device.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liuyadu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemRoleServiceImpl extends BaseServiceImpl<SystemRoleMapper, SystemRole> implements SystemRoleService {
    @Autowired
    private SystemRoleUserMapper systemRoleUserMapper;
    @Autowired
    private SystemUserService systemUserService;

    @Autowired
    private SystemRoleActionMapper systemRoleActionMapper;

    @Autowired
    private SystemRoleMenuMapper systemRoleMenuMapper;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemRole> findPage(PageParams pageParams) {
        SystemRole query = pageParams.mapToBean(SystemRole.class);
        QueryWrapper<SystemRole> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getRoleType()), SystemRole::getRoleType, query.getRoleType())
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), SystemRole::getTenantId, query.getTenantId())
                .likeRight(ObjectUtils.isNotEmpty(query.getRoleCode()), SystemRole::getRoleCode, query.getRoleCode())
                .likeRight(ObjectUtils.isNotEmpty(query.getRoleName()), SystemRole::getRoleName, query.getRoleName())
                .orderByDesc(SystemRole::getCreateTime);
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SystemRole> findList(RequestParams requestParams) {
        SystemRole query = requestParams.mapToBean(SystemRole.class);
        QueryWrapper<SystemRole> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), SystemRole::getTenantId, query.getTenantId());
        List<SystemRole> list = list(queryWrapper);
        return list;
    }

    /**
     * 添加角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public SystemRole add(SystemRole role) {
        if (isExist(role.getRoleCode(),role.getRoleType())) {
            throw new BaseException(String.format("%s编码已存在!", role.getRoleCode()));
        }
        if (role.getStatus() == null) {
            role.setStatus(SystemConstants.ENABLED);
        }
        if (role.getIsPersist() == null) {
            role.setIsPersist(SystemConstants.DISABLED);
        }
        role.setCreateTime(new Date());
        role.setUpdateTime(role.getCreateTime());
        save(role);
        return role;
    }

    /**
     * 更新角色
     *
     * @param role 角色
     * @return
     */
    @Override
    public SystemRole update(SystemRole role) {
        SystemRole saved = getById(role.getRoleId());
        if (role == null) {
            throw new BaseException("信息不存在!");
        }
        if (!saved.getRoleCode().equals(role.getRoleCode())) {
            // 和原来不一致重新检查唯一性
            if (isExist(role.getRoleCode(),role.getRoleType())) {
                throw new BaseException(String.format("%s编码已存在!", role.getRoleCode()));
            }
        }
        role.setUpdateTime(new Date());
        updateById(role);
        return role;
    }

    /**
     * 删除角色
     *
     * @param roleId 角色ID
     * @return
     */
    @Override
    public void remove(Long roleId) {
        if (roleId == null) {
            return;
        }
        SystemRole role = getById(roleId);
        if (role != null && role.getIsPersist().equals(SystemConstants.ENABLED)) {
            throw new BaseException(String.format("默认数据,禁止删除"));
        }
        if(role!=null&&role.getIsSystem().equals(1)){
            throw new BaseException(String.format("系统角色,禁止删除"));
        }
        int count = getCountByRoleId(roleId);
        if (count > 0) {
            throw new BaseException("该角色下存在授权人员,禁止删除!");
        }
        removeById(roleId);
    }

    /**
     * 检测角色编码是否存在
     *
     * @param roleCode
     * @return
     */
    @Override
    public Boolean isExist(String roleCode,Integer roleType) {
        if (StringUtil.isBlank(roleCode)) {
            throw new BaseException("roleCode不能为空!");
        }
        QueryWrapper<SystemRole> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRole::getRoleCode, roleCode)
                .eq(ObjectUtils.isNotEmpty(roleType),SystemRole::getRoleType,roleType);
        return count(queryWrapper) > 0;
    }

    /**
     * 用户添加角色
     *
     * @param userId
     * @param roles
     * @return
     */
    @Override
    public void saveRoles(Long userId, String... roles) {
        if (userId == null || roles == null) {
            return;
        }
        SystemUser user = systemUserService.getById(userId);
        if (user == null) {
            return;
        }
        if (CommonConstants.ROOT.equals(user.getUserName())) {
            throw new BaseException("默认用户无需分配!");
        }
        // 先清空,在添加
        removeRolesByUserId(userId);
        if (roles != null && roles.length > 0) {
            for (String roleId : roles) {
                SystemRoleUser roleUser = new SystemRoleUser();
                roleUser.setUserId(userId);
                roleUser.setRoleId(Long.parseLong(roleId));
                systemRoleUserMapper.insert(roleUser);
            }
            // 批量保存
        }
    }

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     */
    @Override
    public void saveUsers(Long roleId, String... userIds) {
        if (roleId == null || userIds == null) {
            return;
        }
        // 先清空,在添加
        removeUsersByRoleId(roleId);
        if (userIds != null && userIds.length > 0) {
            for (String userId : userIds) {
                SystemRoleUser roleUser = new SystemRoleUser();
                roleUser.setUserId(Long.parseLong(userId));
                roleUser.setRoleId(roleId);
                systemRoleUserMapper.insert(roleUser);
            }
            // 批量保存
        }
    }

    /**
     * 查询角色成员
     *
     * @return
     */
    @Override
    public List<SystemRoleUser> findUsersByRoleId(Long roleId) {
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleUser::getRoleId, roleId);
        return systemRoleUserMapper.selectList(queryWrapper);
    }

    /**
     * 获取角色所有授权组员数量
     *
     * @param roleId
     * @return
     */
    @Override
    public int getCountByRoleId(Long roleId) {
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleUser::getRoleId, roleId);
        int result = systemRoleUserMapper.selectCount(queryWrapper);
        return result;
    }

    /**
     * 获取组员角色数量
     *
     * @param userId
     * @return
     */
    @Override
    public int getCountByUserId(Long userId) {
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleUser::getUserId, userId);
        int result = systemRoleUserMapper.selectCount(queryWrapper);
        return result;
    }

    /**
     * 移除角色所有组员
     *
     * @param roleId
     * @return
     */
    @Override
    public void removeUsersByRoleId(Long roleId) {
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleUser::getRoleId, roleId);
        systemRoleUserMapper.delete(queryWrapper);
    }

    /**
     * 移除组员的所有角色
     *
     * @param userId
     * @return
     */
    @Override
    public void removeRolesByUserId(Long userId) {
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleUser::getUserId, userId);
        systemRoleUserMapper.delete(queryWrapper);
    }

    /**
     * 检测是否存在
     *
     * @param userId
     * @param roleId
     * @return
     */
    @Override
    public Boolean isExist(Long userId, Long roleId) {
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemRoleUser::getRoleId, roleId);
        queryWrapper.lambda().eq(SystemRoleUser::getUserId, userId);
        systemRoleUserMapper.delete(queryWrapper);
        int result = systemRoleUserMapper.selectCount(queryWrapper);
        return result > 0;
    }


    /**
     * 获取组员角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<SystemRole> findRolesByUserId(Long userId) {
        List<SystemRole> roles = systemRoleUserMapper.selectUsersByRoleId(userId);
        return roles;
    }

    /**
     * 获取组员角色
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String,Object> findMapRoleByUserId(Long userId) {
        Map<String,Object> resultMap = Maps.newHashMap();
        QueryWrapper<SystemRole> query = new QueryWrapper();
        List<SystemRole> allRoles = baseMapper.selectList(query);
        Long selectRole = systemRoleUserMapper.selectRoleByUserId(userId);
        resultMap.put("allRoles",allRoles);
        resultMap.put("selectRoles",selectRole);
        return resultMap;
    }


    /**
     * 获取用户角色ID列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Long> findRoleIdsByUserId(Long userId) {
        return systemRoleUserMapper.selectUsersIdByRoleId(userId);
    }

    @Override
    public List<BaseAuthority> findActionByRole(Long roleId, String roleType) {
        return baseMapper.findActionByRole(roleId, roleType);
    }

    @Override
    public Map<String, Object> authorityRoleMenu(Long roleId,Integer roleType, Long tenantId) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<TenantMenuAllRes> tenantMenuAllRes = baseMapper.selectRoleMenuAllList(roleType);
        Collections.sort(tenantMenuAllRes, Comparator.comparing(TenantMenuAllRes::getSort).reversed());
        List<TenantMenuAllRes> tenantMenuGrantList = baseMapper.selectRoleMenuAuthorityList(roleId, roleType);
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

    @Override
    public Map<String, Object> authorityCustRoleMenu(Long roleId,Integer roleType) {
        Map<String, Object> resultMap = Maps.newHashMap();
        List<TenantMenuAllRes> tenantMenuAllRes = baseMapper.selectCustRoleMenuAllList(roleType);
        Collections.sort(tenantMenuAllRes, Comparator.comparing(TenantMenuAllRes::getSort).reversed());
        List<TenantMenuAllRes> tenantMenuGrantList = baseMapper.selectCustRoleMenuAuthorityList(roleId, roleType);
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
    public void grantRoleMenu(Long roleId, String menuIds) {
        if (roleId == null) {
            return;
        }
        SystemRole role = baseMapper.selectById(roleId);
        if (role == null) {
            return;
        }
        deleteGrantRoleMenu(roleId);
        batchGrantRoleMenu(roleId,menuIds != null ? Arrays.asList(StringUtils.commaDelimitedListToStringArray(menuIds)) : null);
    }

    private void deleteGrantRoleMenu(Long roleId){
        QueryWrapper<SystemRoleAction> actionQueryWrapper = new QueryWrapper();
        actionQueryWrapper.lambda().eq(SystemRoleAction::getRoleId, roleId);
        systemRoleActionMapper.delete(actionQueryWrapper);
        QueryWrapper<SystemRoleMenu> menuQueryWrapper = new QueryWrapper();
        menuQueryWrapper.lambda().eq(SystemRoleMenu::getRoleId, roleId);
        systemRoleMenuMapper.delete(menuQueryWrapper);
    }

    private void batchGrantRoleMenu(Long roleId,List<String> menuIds){
        if(menuIds!=null&&menuIds.size()>0) {
            systemRoleActionMapper.batchGrantRoleAction(roleId, menuIds);
            systemRoleMenuMapper.batchGrantRoleMenu(roleId, menuIds);
        }
    }

    @Override
    public Integer selectSystemRoleByUserIdAndTenant(List<Long> userIds, Integer userType) {
        return baseMapper.selectSystemRoleByUserIdAndTenant(userIds,userType);
    }
}
