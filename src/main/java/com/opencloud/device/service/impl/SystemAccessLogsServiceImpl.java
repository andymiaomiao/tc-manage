package com.opencloud.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.entity.SystemAccessLogs;
import com.opencloud.device.mapper.SystemAccessLogsMapper;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.SystemAccessLogsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 通用账号
 *
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemAccessLogsServiceImpl extends BaseServiceImpl<SystemAccessLogsMapper, SystemAccessLogs> implements SystemAccessLogsService {


    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemAccessLogs> findPage(PageParams pageParams) {
        SystemAccessLogs query = pageParams.mapToBean(SystemAccessLogs.class);
        QueryWrapper<SystemAccessLogs> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .like(ObjectUtils.isNotEmpty(query.getUserName()), SystemAccessLogs::getUserName, query.getUserName())
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), SystemAccessLogs::getTenantId, query.getTenantId());
        if(pageParams.getRequestMap().get("startTime")!=null){
            queryWrapper.lambda()
                    .ge(ObjectUtils.isNotEmpty(pageParams.getRequestMap().get("startTime")), SystemAccessLogs::getCreateTime, pageParams.getRequestMap().get("startTime").toString()+" 00:00:00");
        }
        if(pageParams.getRequestMap().get("endTime")!=null){
            queryWrapper.lambda()
                    .le(ObjectUtils.isNotEmpty(pageParams.getRequestMap().get("endTime")), SystemAccessLogs::getCreateTime, pageParams.getRequestMap().get("endTime").toString()+" 23:59:59");
        }
        queryWrapper.lambda().orderByDesc(SystemAccessLogs::getCreateTime);
        return baseMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }
//    /**
//     * 注册账号
//     *
//     * @param account
//     * @param password
//     * @param accountType
//     * @param status
//     * @param domain
//     * @param registerIp
//     * @return
//     */
//    @Override
//    public SystemAccount register(String account, String password, String accountType, Integer status, String domain, String registerIp) {
//        if (isExist(account, accountType, domain)) {
//            // 账号已被注册
//            throw new RuntimeException(String.format("account=[%s],entity=[%s]", account, domain));
//        }
//        //加密
//        String encodePassword = passwordEncoder.encode(password);
//        SystemAccount systemAccount = new SystemAccount(account, encodePassword, accountType, domain, registerIp);
//        systemAccount.setCreateTime(new Date());
//        systemAccount.setUpdateTime(systemAccount.getCreateTime());
//        systemAccount.setStatus(status);
//        save(systemAccount);
//        // todo 加上生成 user信息
//        return systemAccount;
//    }
//
//
//    /**
//     * 检测账号是否存在
//     *
//     * @param account
//     * @param accountType
//     * @param domain
//     * @return
//     */
//    @Override
//    public Boolean isExist(String account, String accountType, String domain) {
//        QueryWrapper<SystemAccount> queryWrapper = new QueryWrapper();
//        queryWrapper.lambda()
//                .eq(SystemAccount::getAccount, account)
//                .eq(SystemAccount::getAccountType, accountType)
//                .eq(SystemAccount::getDomain, domain);
//        int count = count(queryWrapper);
//        return count > 0 ? true : false;
//    }
//
//
//    /**
//     * 更新账号状态
//     *
//     * @param accountId
//     * @param status
//     */
//    @Override
//    public Boolean updateStatus(Long accountId, Integer status) {
//        SystemAccount systemAccount = new SystemAccount();
//        systemAccount.setAccountId(accountId);
//        systemAccount.setUpdateTime(new Date());
//        systemAccount.setStatus(status);
//        return updateById(systemAccount);
//    }
//
//    /**
//     * 根据用户更新账户状态
//     *
//     * @param accountId
//     * @param domain
//     * @param status
//     */
//    @Override
//    public Boolean updateStatusById(Long accountId, String domain, Integer status) {
//        if (status == null) {
//            return false;
//        }
//        SystemAccount systemAccount = new SystemAccount();
//        systemAccount.setUpdateTime(new Date());
//        systemAccount.setStatus(status);
//        QueryWrapper<SystemAccount> wrapper = new QueryWrapper();
//        wrapper.lambda()
//                .eq(SystemAccount::getDomain, domain)
//                .eq(SystemAccount::getAccountId, accountId);
//        return update(systemAccount, wrapper);
//    }
//
//    /**
//     * 重置用户密码
//     *
//     * @param accountId
//     * @param domain
//     * @param password
//     */
//    @Override
//    public Boolean updatePasswordById(Long accountId, String domain, String password) {
//        SystemAccount systemAccount = new SystemAccount();
//        systemAccount.setUpdateTime(new Date());
//        systemAccount.setPassword(passwordEncoder.encode(password));
//        QueryWrapper<SystemAccount> wrapper = new QueryWrapper();
//        wrapper.lambda()
//                .in(SystemAccount::getAccountType, SystemConstants.ACCOUNT_TYPE_USERNAME, SystemConstants.ACCOUNT_TYPE_EMAIL, SystemConstants.ACCOUNT_TYPE_MOBILE)
//                .eq(SystemAccount::getAccountId, accountId)
//                .eq(SystemAccount::getDomain, domain);
//        return update(systemAccount, wrapper);
//    }
//
//    /**
//     * 重置用户密码
//     *
//     * @param accountId
//     * @param domain
//     * @param password
//     */
//    @Override
//    public Boolean currentUpdatePasswordById(Long accountId, String domain, String oldPassword, String password) {
//        QueryWrapper<SystemAccount> wrapper = new QueryWrapper();
//        wrapper.lambda()
//                .in(SystemAccount::getAccountType, SystemConstants.ACCOUNT_TYPE_USERNAME, SystemConstants.ACCOUNT_TYPE_EMAIL, SystemConstants.ACCOUNT_TYPE_MOBILE)
//                .eq(SystemAccount::getAccountId, accountId)
//                .eq(SystemAccount::getDomain, domain);
//        SystemAccount systemAccount1 = baseMapper.selectOne(wrapper);
//        if (systemAccount1 != null) {
//            if (passwordEncoder.matches(oldPassword, systemAccount1.getPassword())) {
//                SystemAccount systemAccount = new SystemAccount();
//                systemAccount.setUpdateTime(new Date());
//                systemAccount.setPassword(passwordEncoder.encode(password));
//
//                return update(systemAccount, wrapper);
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 根据用户ID删除账号
//     *
//     * @param accountId
//     * @param domain
//     * @return
//     */
//    @Override
//    public Boolean removeById(Long accountId, String domain) {
//        QueryWrapper<SystemAccount> wrapper = new QueryWrapper();
//        wrapper.lambda()
//                .eq(SystemAccount::getAccountId, accountId)
//                .eq(SystemAccount::getDomain, domain);
//        return remove(wrapper);
//    }
}
