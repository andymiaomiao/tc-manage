package com.weiqisen.tc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.weiqisen.tc.entity.SystemAccount;
import com.weiqisen.tc.entity.SystemRole;
import com.weiqisen.tc.entity.SystemRoleUser;
import com.weiqisen.tc.entity.SystemUser;
import com.weiqisen.tc.exception.BaseException;
import com.weiqisen.tc.form.req.UserReq;
import com.weiqisen.tc.form.res.UserRes;
import com.weiqisen.tc.mapper.SystemRoleUserMapper;
import com.weiqisen.tc.mapper.SystemUserMapper;
import com.weiqisen.tc.model.CommonConstants;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.SystemConstants;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.security.BaseAuthority;
import com.weiqisen.tc.security.BaseSecurityConstants;
import com.weiqisen.tc.service.SystemAccountService;
import com.weiqisen.tc.service.SystemActionService;
import com.weiqisen.tc.service.SystemRoleService;
import com.weiqisen.tc.service.SystemUserService;
import com.weiqisen.tc.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: weiqisen
 * @date: 2018/10/24 16:33
 * @description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemUserServiceImpl extends BaseServiceImpl<SystemUserMapper, SystemUser> implements SystemUserService {

    @Autowired
    private SystemRoleService roleService;
    @Autowired
    private SystemActionService systemActionService;
    @Autowired
    private SystemAccountService systemAccountService;
    @Autowired
    private SystemRoleUserMapper systemRoleUserMapper;


    private final String ACCOUNT_DOMAIN = SystemConstants.ACCOUNT_DOMAIN_ADMIN;

    /**
     * 添加系统用户
     *
     * @param systemUser
     * @return
     */
    @Override
    public void add(SystemUser systemUser) {
        if (getByUsername(systemUser.getUserName()) != null) {
            throw new BaseException("用户名:" + systemUser.getUserName() + "已存在!");
        }
        systemUser.setCreateTime(new Date());
        systemUser.setUpdateTime(systemUser.getCreateTime());
        //保存系统用户信息
        save(systemUser);
        //默认注册用户名账户
        SystemAccount register = null;
//        if (StringUtil.matchMobile(systemUser.getMobile())) {
//            //注册手机号账号登陆
//            register = systemAccountService.register(systemUser.getMobile(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_MOBILE, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
//        }else if (StringUtil.matchEmail(systemUser.getEmail())) {
//            //注册email账号登陆
//            register = systemAccountService.register(systemUser.getEmail(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_EMAIL, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
//        }
//
//        if (register == null) {
//            register = systemAccountService.register(systemUser.getUserName(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_USERNAME, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
//        }
        QueryWrapper<SystemAccount> systemAccountQueryWrapper = new QueryWrapper<>();
        systemAccountQueryWrapper.lambda()
                .eq(SystemAccount::getAccount,systemUser.getUserName())
                .eq(SystemAccount::getMobile,systemUser.getMobile());
        List<SystemAccount> list = systemAccountService.list(systemAccountQueryWrapper);
        if(list!=null&&list.size()>0){
            if(list.size()==1){
                register = list.get(0);
            }else{
                throw new BaseException("该账号或手机已经多次注册");
            }
        }else {
            register = systemAccountService.register(systemUser.getUserName(), systemUser.getMobile(), systemUser.getEmail(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_MOBILE, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
        }
        if (register != null && register.getAccountId() != null) {
            systemUser.setAccountId(register.getAccountId());
            update(systemUser);
        }
    }

    /**
     * 添加系统用户
     *
     * @param systemUser
     * @return
     */
    @Override
    public void tenantSave(SystemUser systemUser) {
        List<SystemUser> userList = getUserByTenant(systemUser);
        if (userList != null&&userList.size()>0) {
            List<Long> userIds = userList.stream().map(m -> m.getUserId()).collect(Collectors.toList());
            Integer systemRoleCount = roleService.selectSystemRoleByUserIdAndTenant(userIds, systemUser.getType());
            if(systemRoleCount>0) {
                throw new BaseException(String.format("该手机号%s已经注册主体账号!",systemUser.getMobile()));
            }
        }else {
            systemUser.setCreateTime(new Date());
            systemUser.setUpdateTime(systemUser.getCreateTime());
            //保存系统用户信息
            save(systemUser);
            //默认注册用户名账户
            SystemAccount register = null;
//        if (StringUtil.matchMobile(systemUser.getMobile())) {
//            //注册手机号账号登陆
//            register = systemAccountService.register(systemUser.getMobile(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_MOBILE, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
//        }else if (StringUtil.matchEmail(systemUser.getEmail())) {
//            //注册email账号登陆
//            register = systemAccountService.register(systemUser.getEmail(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_EMAIL, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
//        }
//
//        if (register == null) {
//            register = systemAccountService.register(systemUser.getUserName(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_USERNAME, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
//        }
            register = systemAccountService.register(systemUser.getUserName(), systemUser.getMobile(), systemUser.getEmail(), systemUser.getPassword(), SystemConstants.ACCOUNT_TYPE_MOBILE, systemUser.getStatus(), ACCOUNT_DOMAIN, null);
            if (register != null && register.getAccountId() != null) {
                systemUser.setAccountId(register.getAccountId());
                update(systemUser);
            }
        }
    }

    /**
     * 更新系统用户
     *
     * @param systemUser
     * @return
     */
    @Override
    public Boolean update(SystemUser systemUser) {
        if (systemUser == null || systemUser.getUserId() == null) {
            return false;
        }
        if (systemUser.getStatus() != null) {
            systemAccountService.updateStatusById(systemUser.getAccountId(), ACCOUNT_DOMAIN, systemUser.getStatus());
        }
        return updateById(systemUser);
    }

    /**
     * 删除用户
     *
     * @param userId 角色ID
     * @return
     */
    @Override
    public void remove(Long userId) {
        if (userId == null) {
            return;
        }
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SystemRoleUser::getUserId,userId);
        systemRoleUserMapper.delete(queryWrapper);
        removeById(userId);
    }

    /**
     * 批量删除用户
     *
     * @param userIds 角色ID
     * @return
     */

    @Override
    public void removeBatch(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return;
        }
        List<String> ids = Arrays.asList(userIds.split(","));
        QueryWrapper<SystemRoleUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(SystemRoleUser::getUserId,ids);
        systemRoleUserMapper.delete(queryWrapper);
        removeByIds(ids);
    }

    /**
     * 添加第三方登录用户
     *
     * @param systemUser
     * @param accountType
     */
    @Override
    public void addThirdParty(SystemUser systemUser, String accountType) {
        if (!systemAccountService.isExist(systemUser.getUserName(), accountType, ACCOUNT_DOMAIN)) {
            systemUser.setUserType(SystemConstants.USER_TYPE_ADMIN);
            systemUser.setCreateTime(new Date());
            systemUser.setUpdateTime(systemUser.getCreateTime());
            //保存系统用户信息
            save(systemUser);
            // 注册账号信息
            SystemAccount register = systemAccountService.register(systemUser.getUserName(),systemUser.getMobile(),systemUser.getEmail(), systemUser.getPassword(), accountType, SystemConstants.ACCOUNT_STATUS_NORMAL, ACCOUNT_DOMAIN, null);
            if (register != null && register.getAccountId() != null) {
                systemUser.setAccountId(register.getAccountId());
                update(systemUser);
            }
        }
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    @Override
    public void updatePassword(Long userId, String password) {
        SystemUser systemUser = baseMapper.selectById(userId);
        systemAccountService.updatePasswordById(systemUser.getAccountId(), ACCOUNT_DOMAIN, password);
    }

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    @Override
    public Boolean currentUpdatePassword(Long userId, String oldPassword, String password) {
        SystemUser systemUser = baseMapper.selectById(userId);
        return systemAccountService.currentUpdatePasswordById(systemUser.getAccountId(), ACCOUNT_DOMAIN, oldPassword, password);
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<Map<String, Object>> findPage(PageParams pageParams) {
        IPage<Map<String, Object>> resultMapPage = baseMapper.selectUserPage(pageParams, pageParams.getRequestMap());
        return resultMapPage;
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<Map<String, Object>> findAdminPage(PageParams pageParams) {
        IPage<Map<String, Object>> resultMapPage = baseMapper.selectAdminUserPage(pageParams, pageParams.getRequestMap());
        return resultMapPage;
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<SystemUser> findList(UserReq userReq) {
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        if (userReq.getUserIds() != null && userReq.getUserIds().size() > 0) {
            queryWrapper.lambda().in(SystemUser::getUserId, userReq.getUserIds());
        }
        if (userReq.getType() != null) {
            queryWrapper.lambda().eq(SystemUser::getType, userReq.getType());
        }
        List<SystemUser> list = list(queryWrapper);
        return list;
    }

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param accountId
     * @return
     */
    @Override
    public UserRes getUserInfo(Long accountId, String type) {
        //查询系统用户资料
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SystemUser::getAccountId, accountId)
                .eq(SystemUser::getType, type);
        SystemUser systemUser = this.baseMapper.selectOne(queryWrapper);
        if (systemUser == null) {
            throw new BaseException("用户信息不存在!");
        }

        // 用户权限列表
        List<BaseAuthority> authorities = Lists.newArrayList();
        // 用户角色列表
        List<Map> roles = Lists.newArrayList();
        List<SystemRole> rolesList = roleService.findRolesByUserId(systemUser.getUserId());
        if (rolesList != null) {
            for (SystemRole role : rolesList) {
                Map roleMap = Maps.newHashMap();
                roleMap.put("roleId", role.getRoleId());
                roleMap.put("roleCode", role.getRoleCode());
                roleMap.put("roleName", role.getRoleName());
                // 用户角色详情
                roles.add(roleMap);
                // 加入角色标识
                BaseAuthority authority = new BaseAuthority(role.getRoleId().toString(), BaseSecurityConstants.AUTHORITY_PREFIX_ROLE + role.getRoleCode(), null, "role");
                authorities.add(authority);
            }
        }

        // 加入用户权限
        List<BaseAuthority> userGrantedAuthority = systemActionService.findActionByUser(systemUser.getUserId(), CommonConstants.ROOT.equals(systemUser.getUserName()), type);
        if (userGrantedAuthority != null && userGrantedAuthority.size() > 0) {
            authorities.addAll(userGrantedAuthority);
        }
        UserRes userInfo = new UserRes();
        // 昵称
        userInfo.setNickName(systemUser.getNickName());
        //手机号
        userInfo.setMobile(systemUser.getMobile());
        //邮箱
        userInfo.setEmail(systemUser.getEmail());
        //邮箱
        userInfo.setCity(systemUser.getCity());
        //邮箱
        userInfo.setUserDesc(systemUser.getUserDesc());
        // 用户id
        userInfo.setUserId(systemUser.getUserId());
        // 头像
        userInfo.setAvatar(systemUser.getAvatar());
        // 权限信息
        userInfo.setAuthorities(authorities);
        userInfo.setRoles(roles);
        return userInfo;
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    @Override
    public SystemUser getByUsername(String username) {
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SystemUser::getUserName, username);
        SystemUser saved = getOne(queryWrapper);
        return saved;
    }


    /**
     * 依据登录名查询系统用户信息
     *
     * @param systemUser
     * @return
     */
    @Override
    public List<SystemUser> getUserByTenant(SystemUser systemUser) {
        List<SystemUser> userList = new ArrayList<>();
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper();
        if(ObjectUtils.isNotEmpty(systemUser.getUserName()) ||
                ObjectUtils.isNotEmpty(systemUser.getMobile()) ||
                ObjectUtils.isNotEmpty(systemUser.getEmail())) {
            queryWrapper.lambda()
                    .eq(SystemUser::getTenantId, systemUser.getTenantId())
                    .eq(SystemUser::getType, systemUser.getType())
                    .and(wrapper ->
                        wrapper
                            .eq(ObjectUtils.isNotEmpty(systemUser.getUserName()),SystemUser::getUserName, systemUser.getUserName())
                            .or()
                            .eq(ObjectUtils.isNotEmpty(systemUser.getEmail()),SystemUser::getEmail, systemUser.getEmail())
                            .or()
                            .eq(ObjectUtils.isNotEmpty(systemUser.getMobile()),SystemUser::getMobile, systemUser.getMobile())
                    );
            userList.addAll(baseMapper.selectList(queryWrapper));
        }
        return userList;
    }


    /**
     * 支持系统用户名、手机号、email登陆
     *
     * @param account
     * @return
     */
    @Override
    public UserRes login(String account, String thirdParty, String type) {
        if (StringUtil.isBlank(account)) {
            return null;
        }
        SystemAccount systemAccount = null;
        if (StringUtil.isNotBlank(thirdParty)) {
            // 第三方登录
            systemAccount = systemAccountService.getThirdParty(account, thirdParty, ACCOUNT_DOMAIN);
        } else {
            // 手机号登陆
            if (StringUtil.matchMobile(account)) {
                systemAccount = systemAccountService.getMobile(account, SystemConstants.ACCOUNT_TYPE_MOBILE, ACCOUNT_DOMAIN);
            }else if (StringUtil.matchEmail(account)) {
                // 邮箱登陆
                systemAccount = systemAccountService.getEmail(account, SystemConstants.ACCOUNT_TYPE_EMAIL, ACCOUNT_DOMAIN);
            }else {
                //用户名登录
                systemAccount = systemAccountService.getUserName(account, SystemConstants.ACCOUNT_TYPE_USERNAME, ACCOUNT_DOMAIN);
            }
        }

        // 获取用户详细信息
        if (systemAccount != null) {
            // 用户权限信息
            UserRes userInfo = getUserInfo(systemAccount.getAccountId(), type);
            // 复制账号信息
            BeanUtils.copyProperties(systemAccount, userInfo);
            return userInfo;
        }else{
            throw new BaseException(String.format("%s该登陆信息不存在!", account));
        }
    }

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemUser> findUserPage(PageParams pageParams) {
        SystemUser query = pageParams.mapToBean(SystemUser.class);
        QueryWrapper<SystemUser> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), SystemUser::getTenantId, query.getTenantId())
                .eq(ObjectUtils.isNotEmpty(query.getType()), SystemUser::getType, query.getType())
                .like(ObjectUtils.isNotEmpty(query.getNickName()), SystemUser::getNickName, query.getNickName())
                .orderByDesc(SystemUser::getCreateTime);
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    @Override
    public void lockUserAll() {
        QueryWrapper<SystemAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().orderByDesc(SystemAccount::getStatus);
        List<SystemAccount> systemUserList = systemAccountService.list(queryWrapper);
        if(systemUserList!=null&&systemUserList.size()>0){
            Optional<SystemAccount> first = systemUserList.stream().filter(f -> f.getStatus() == 1).findFirst();
            if(first.isPresent()){
                systemUserList.stream().forEach(item-> item.setStatus(2));
                systemAccountService.updateBatchById(systemUserList);
            }
        }
    }

    @Override
    public void lockUserUpdateAll(SystemUser systemUser) {
        QueryWrapper<SystemAccount> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().orderByDesc(SystemAccount::getStatus);
        List<SystemAccount> systemUserList = systemAccountService.list(queryWrapper);
        if(systemUserList!=null&&systemUserList.size()>0){
            systemUserList.stream().forEach(item-> item.setUpdateTime(systemUser.getUpdateTime()));
            systemAccountService.updateBatchById(systemUserList);
        }
    }
}
