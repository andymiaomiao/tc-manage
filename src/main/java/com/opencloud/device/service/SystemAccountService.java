package com.opencloud.device.service;


import com.opencloud.device.entity.SystemAccount;
import com.opencloud.device.mybatis.base.service.IBaseService;

/**
 * 系统用户登录账号管理
 * 支持多账号登陆
 *
 * @author liuyadu
 */
public interface SystemAccountService extends IBaseService<SystemAccount> {

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    SystemAccount getThirdParty(String account, String accountType, String domain);

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    SystemAccount getUserName(String account, String accountType, String domain);

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    SystemAccount getMobile(String account, String accountType, String domain);

    /**
     * 获取账号信息
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    SystemAccount getEmail(String account, String accountType, String domain);

    /**
     * 注册账号
     *
     * @param account
     * @param mobile
     * @param password
     * @param accountType
     * @param status
     * @param domain
     * @param registerIp
     * @return
     */
    SystemAccount register(String account, String mobile, String email, String password, String accountType, Integer status, String domain, String registerIp);


    /**
     * 检查账号是否存在
     *
     * @param account
     * @param accountType
     * @param domain
     * @return
     */
    Boolean isExist(String account, String accountType, String domain);


    /**
     * 更新账号状态
     *
     * @param accountId
     * @param status
     * @return
     */
    Boolean updateStatus(Long accountId, Integer status);

    /**
     * 根据用户更新账户状态
     *
     * @param accountId
     * @param domain
     * @param status
     * @return
     */
    Boolean updateStatusById(Long accountId, String domain, Integer status);

    /**
     * 重置用户密码
     *
     * @param accountId
     * @param domain
     * @param password
     * @return
     */
    Boolean updatePasswordById(Long accountId, String domain, String password);


    /**
     * 重置用户密码
     *
     * @param accountId
     * @param domain
     * @param password
     * @return
     */
    Boolean currentUpdatePasswordById(Long accountId, String domain, String oldPassword, String password);

    /**
     * 根据用户ID删除账号
     *
     * @param accountId
     * @param domain
     * @return
     */
    Boolean removeById(Long accountId, String domain);
}
