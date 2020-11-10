package com.opencloud.device.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.entity.IotDeviceLog;
import com.opencloud.device.entity.IotDeviceTask;
import com.opencloud.device.form.req.DeviceLofInfoRes;
import com.opencloud.device.mapper.IotDeviceLogMapper;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.IotDeviceLogService;
import com.opencloud.device.service.IotDeviceService;
import com.opencloud.device.service.IotDeviceTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;


/**
 * @author liuyadu
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IotDeviceLogServiceImpl extends BaseServiceImpl<IotDeviceLogMapper, IotDeviceLog> implements IotDeviceLogService {
    @Autowired
    private IotDeviceTaskService iotDeviceTaskService;
    @Autowired
    private IotDeviceService iotDeviceService;

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<Map<String, Object>> findPage(PageParams pageParams) {
        IPage<Map<String, Object>> logPage = baseMapper.selectLogPage(pageParams, pageParams.getRequestMap());
        return logPage;
    }

    @Override
    public void getDeviceLogToCloud(DeviceLofInfoRes deviceLofInfoRes) {
        if(deviceLofInfoRes.getTaskId()!=null) {
            IotDeviceTask deviceTaskInfo = iotDeviceTaskService.getById(deviceLofInfoRes.getTaskId());
            if (deviceTaskInfo != null) {
                deviceTaskInfo.setCode(deviceLofInfoRes.getTaskCode());
                deviceTaskInfo.setStatus(deviceLofInfoRes.getTaskStatus());
                deviceTaskInfo.setContent(JSON.toJSONString(deviceLofInfoRes));
                iotDeviceTaskService.updateById(deviceTaskInfo);
            }
        }
        IotDevice iotDevice = new IotDevice();
        if(deviceLofInfoRes.getTotalSize().equals(deviceLofInfoRes.getCompletedSize())){
            iotDevice.setUploadStatus(1);
            iotDevice.setUploadProgress(0d);
        }else {
            BigDecimal bigDecimal1 = new BigDecimal(deviceLofInfoRes.getCompletedSize());
            BigDecimal bigDecimal2 = new BigDecimal(deviceLofInfoRes.getTotalSize());
            if(bigDecimal2.compareTo(bigDecimal1)>0){
                iotDevice.setUploadStatus(0);
                BigDecimal bigDecimal = bigDecimal1.divide(bigDecimal2,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                iotDevice.setUploadProgress(bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue());
            }else{
                iotDevice.setUploadStatus(1);
                iotDevice.setUploadProgress(0d);
            }
        }
        QueryWrapper<IotDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(IotDevice::getDeviceSn,deviceLofInfoRes.getDeviceSn());
        IotDevice one = iotDeviceService.getOne(queryWrapper);
        iotDevice.setDeviceId(one.getDeviceId());
        iotDeviceService.updateById(iotDevice);
        IotDeviceLog iotDeviceLog = new IotDeviceLog();
        iotDeviceLog.setDeviceId(one.getDeviceId());
        iotDeviceLog.setFileName(deviceLofInfoRes.getFileName());
        iotDeviceLog.setRealName(deviceLofInfoRes.getRealName());
        iotDeviceLog.setFileSize(deviceLofInfoRes.getTotalSize());
        iotDeviceLog.setFileUrl(deviceLofInfoRes.getUrl());
        baseMapper.insert(iotDeviceLog);
    }

    //
//    /**
//     * 查询列表
//     *
//     * @return
//     */
//    @Override
//    public List<IotDeviceLog> findList() {
//        List<IotDeviceLog> list = list(new QueryWrapper<>());
//        return list;
//    }
//
//    /**
//     * 添加角色
//     *
//     * @param role 角色
//     * @return
//     */
//    @Override
//    public IotDeviceLog add(IotDeviceLog role) {
//        if (isExist(role.getRoleCode())) {
//            throw new BaseException(String.format("%s编码已存在!", role.getRoleCode()));
//        }
//        if (role.getStatus() == null) {
//            role.setStatus(SystemConstants.ENABLED);
//        }
//        if (role.getIsPersist() == null) {
//            role.setIsPersist(SystemConstants.DISABLED);
//        }
//        role.setCreateTime(new Date());
//        role.setUpdateTime(role.getCreateTime());
//        save(role);
//        return role;
//    }
//
//    /**
//     * 更新角色
//     *
//     * @param role 角色
//     * @return
//     */
//    @Override
//    public IotDeviceLog update(IotDeviceLog role) {
//        IotDeviceLog saved = getById(role.getRoleId());
//        if (role == null) {
//            throw new BaseException("信息不存在!");
//        }
//        if (!saved.getRoleCode().equals(role.getRoleCode())) {
//            // 和原来不一致重新检查唯一性
//            if (isExist(role.getRoleCode())) {
//                throw new BaseException(String.format("%s编码已存在!", role.getRoleCode()));
//            }
//        }
//        role.setUpdateTime(new Date());
//        updateById(role);
//        return role;
//    }
//
//    /**
//     * 删除角色
//     *
//     * @param roleId 角色ID
//     * @return
//     */
//    @Override
//    public void remove(Long roleId) {
//        if (roleId == null) {
//            return;
//        }
//        IotDeviceLog role = getById(roleId);
//        if (role != null && role.getIsPersist().equals(SystemConstants.ENABLED)) {
//            throw new BaseException(String.format("默认数据,禁止删除"));
//        }
//        int count = getCountByRoleId(roleId);
//        if (count > 0) {
//            throw new BaseException("该角色下存在授权人员,禁止删除!");
//        }
//        removeById(roleId);
//    }
//
//    /**
//     * 检测角色编码是否存在
//     *
//     * @param roleCode
//     * @return
//     */
//    @Override
//    public Boolean isExist(String roleCode) {
//        if (StringUtil.isBlank(roleCode)) {
//            throw new BaseException("roleCode不能为空!");
//        }
//        QueryWrapper<IotDeviceLog> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceLog::getRoleCode, roleCode);
//        return count(queryWrapper) > 0;
//    }
//
//    /**
//     * 用户添加角色
//     *
//     * @param userId
//     * @param roles
//     * @return
//     */
//    @Override
//    public void saveRoles(Long userId, String... roles) {
//        if (userId == null || roles == null) {
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
//        if (roles != null && roles.length > 0) {
//            for (String roleId : roles) {
//                IotDeviceLogUser roleUser = new IotDeviceLogUser();
//                roleUser.setUserId(userId);
//                roleUser.setRoleId(Long.parseLong(roleId));
//                systemRoleUserMapper.insert(roleUser);
//            }
//            // 批量保存
//        }
//    }
//
//    /**
//     * 角色添加成员
//     *
//     * @param roleId
//     * @param userIds
//     */
//    @Override
//    public void saveUsers(Long roleId, String... userIds) {
//        if (roleId == null || userIds == null) {
//            return;
//        }
//        // 先清空,在添加
//        removeUsersByRoleId(roleId);
//        if (userIds != null && userIds.length > 0) {
//            for (String userId : userIds) {
//                IotDeviceLogUser roleUser = new IotDeviceLogUser();
//                roleUser.setUserId(Long.parseLong(userId));
//                roleUser.setRoleId(roleId);
//                systemRoleUserMapper.insert(roleUser);
//            }
//            // 批量保存
//        }
//    }
//
//    /**
//     * 查询角色成员
//     *
//     * @return
//     */
//    @Override
//    public List<IotDeviceLogUser> findUsersByRoleId(Long roleId) {
//        QueryWrapper<IotDeviceLogUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceLogUser::getRoleId, roleId);
//        return systemRoleUserMapper.selectList(queryWrapper);
//    }
//
//    /**
//     * 获取角色所有授权组员数量
//     *
//     * @param roleId
//     * @return
//     */
//    @Override
//    public int getCountByRoleId(Long roleId) {
//        QueryWrapper<IotDeviceLogUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceLogUser::getRoleId, roleId);
//        int result = systemRoleUserMapper.selectCount(queryWrapper);
//        return result;
//    }
//
//    /**
//     * 获取组员角色数量
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public int getCountByUserId(Long userId) {
//        QueryWrapper<IotDeviceLogUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceLogUser::getUserId, userId);
//        int result = systemRoleUserMapper.selectCount(queryWrapper);
//        return result;
//    }
//
//    /**
//     * 移除角色所有组员
//     *
//     * @param roleId
//     * @return
//     */
//    @Override
//    public void removeUsersByRoleId(Long roleId) {
//        QueryWrapper<IotDeviceLogUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceLogUser::getRoleId, roleId);
//        systemRoleUserMapper.delete(queryWrapper);
//    }
//
//    /**
//     * 移除组员的所有角色
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public void removeRolesByUserId(Long userId) {
//        QueryWrapper<IotDeviceLogUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceLogUser::getUserId, userId);
//        systemRoleUserMapper.delete(queryWrapper);
//    }
//
//    /**
//     * 检测是否存在
//     *
//     * @param userId
//     * @param roleId
//     * @return
//     */
//    @Override
//    public Boolean isExist(Long userId, Long roleId) {
//        QueryWrapper<IotDeviceLogUser> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda().eq(IotDeviceLogUser::getRoleId, roleId);
//        queryWrapper.lambda().eq(IotDeviceLogUser::getUserId, userId);
//        systemRoleUserMapper.delete(queryWrapper);
//        int result = systemRoleUserMapper.selectCount(queryWrapper);
//        return result > 0;
//    }
//
//
//    /**
//     * 获取组员角色
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public List<IotDeviceLog> findRolesByUserId(Long userId,Long tenantId) {
//        List<IotDeviceLog> roles = systemRoleUserMapper.selectUsersByRoleId(userId,tenantId);
//        return roles;
//    }
//
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
//
//    @Override
//    public List<BaseAuthority> findActionByRole(Long roleId,Long tenantId) {
//        return baseMapper.findActionByRole(roleId,tenantId);
//    }
}
