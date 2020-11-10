package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.entity.SystemUser;
import com.opencloud.device.form.req.UserReq;
import com.opencloud.device.form.res.UserRes;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.mybatis.base.service.IBaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;


/**
 * 系统用户资料管理
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:38
 * @description:
 */
public interface SystemUserService extends IBaseService<SystemUser> {

    /**
     * 添加用户信息
     *
     * @param systemUser
     * @return
     */
    void add(SystemUser systemUser);

    /**
     * 添加用户信息
     *
     * @param systemUser
     * @return
     */
    void tenantSave(SystemUser systemUser);

    /**
     * 更新系统用户
     *
     * @param systemUser
     * @return
     */
    void update(SystemUser systemUser);

    /**
     * 删除用户
     *
     * @param userId 角色ID
     * @return
     */
    void remove(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 角色ID
     * @return
     */
    void removeBatch(String userIds);

    /**
     * 添加第三方登录用户
     *
     * @param systemUser
     * @param accountType
     * @param
     */
    void addThirdParty(SystemUser systemUser, String accountType);

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    void updatePassword(Long userId, String password);

    /**
     * 更新密码
     *
     * @param userId
     * @param password
     */
    Boolean currentUpdatePassword(Long userId, String oldPassword, String password);

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<Map<String, Object>> findPage(PageParams pageParams);

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<Map<String, Object>> findAdminPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<SystemUser> findList(UserReq userReq);

    /**
     * 根据用户ID获取用户信息和权限
     *
     * @param userId
     * @return
     */
    UserRes getUserInfo(Long userId, String type);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param username
     * @return
     */
    SystemUser getByUsername(String username);

    /**
     * 依据登录名查询系统用户信息
     *
     * @param systemUser
     * @return
     */
    List<SystemUser> getUserByTenant(SystemUser systemUser);

    /**
     * 支持密码、手机号、email登陆
     * 其他方式没有规则，无法自动识别。需要单独开发
     *
     * @param account    登陆账号
     * @param thirdParty
     * @return
     */
    UserRes login(String account, String thirdParty, String type);

    /**
     * 获取所有用户列表
     *
     * @return
     */
    IPage<SystemUser> findUserPage(PageParams pageParams);

    void lockUserAll();

    void lockUserUpdateAll(SystemUser systemUser);
}
