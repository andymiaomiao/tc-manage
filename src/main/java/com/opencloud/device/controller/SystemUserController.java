package com.opencloud.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.SystemUser;
import com.opencloud.device.form.req.UserReq;
import com.opencloud.device.model.CommonConstants;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.service.SystemRoleService;
import com.opencloud.device.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-24 16:48
 */
@Api(tags = "用户中心")
@RestController
@AllArgsConstructor
public class SystemUserController {

    private SystemUserService systemUserService;

    private SystemRoleService systemRoleService;

    /**
     * 系统分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/user/page")
    public ResultBody<Page<Map<String, Object>>> getPage(@RequestParam(required = false) Map map) {
        map.put("type", CommonConstants.DEVICE_MANAGE_TYPE);
        return ResultBody.ok().data(systemUserService.findPage(new PageParams(map)));
    }

    /**
     * 获取所有用户列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表")
    @GetMapping("/user/list")
    public ResultBody<List<SystemUser>> getList(UserReq userReq) {
        return ResultBody.ok().data(systemUserService.findList(userReq));
    }

    /**
     * 获取用户角色
     *
     * @param userId
     * @return
     */
    @OperationLog(value = "分配角色")
    @ApiOperation(value = "获取用户已分配角色", notes = "获取用户已分配角色")
    @GetMapping("/user/roles")
    public ResultBody<Map<String,Object>> getUserRoles(
            @RequestParam(value = "userId") Long userId
    ) {
        return ResultBody.ok().data(systemRoleService.findMapRoleByUserId(userId));
    }

    /**
     * 添加/更新系统用户
     *
     * @param userId
     * @param nickName
     * @param status
     * @param userType
     * @param email
     * @param mobile
     * @param userDesc
     * @param avatar
     * @return
     */
    @OperationLog(value = "添加/编辑用户")
    @ApiOperation(value = "添加/编辑用户", notes = "添加/编辑用户")
    @PostMapping("/user/save")
    public ResultBody save(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "userName") String userName,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "status") Integer status,
            @RequestParam(value = "userType",defaultValue = "10",required = false) String userType,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "mobile", required = false) String mobile,
            @RequestParam(value = "userDesc", required = false) String userDesc,
            @RequestParam(value = "avatar", required = false) String avatar
    ) {
        SystemUser user = new SystemUser();
        user.setUserId(userId);
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setPassword(password);
        user.setUserType(userType);
        user.setEmail(email);
        user.setMobile(mobile);
        user.setUserDesc(userDesc);
        user.setAvatar(avatar);
        user.setStatus(status);
        user.setType(CommonConstants.DEVICE_MANAGE_TYPE);
        if(userId!=null){
            systemUserService.update(user);
        }else {
            systemUserService.add(user);
        }
        return ResultBody.ok();
    }

    @OperationLog(value = "修改用户密码")
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @PostMapping("/user/update/password")
    public ResultBody updatePassword(@RequestParam("userId") Long userId,@RequestParam("password") String password,@RequestParam("confirmPassword") String confirmPassword){
        if(password.equals(confirmPassword)){
            SystemUser user = new SystemUser();
            user.setUserId(userId);
            user.setPassword(password);
            systemUserService.updatePassword(userId, password);
        }
        return ResultBody.ok();
    }

    @OperationLog(value = "删除用户")
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @PostMapping("/user/remove")
    public ResultBody remove(@RequestParam("userId") Long userId){
        systemUserService.remove(userId);
        return ResultBody.ok();
    }

    @OperationLog(value = "批量删除用户")
    @ApiOperation(value = "批量删除用户", notes = "批量删除用户")
    @PostMapping("/user/batch/remove")
    public ResultBody removeBatch(@RequestParam("userIds") String userIds){
        systemUserService.removeBatch(userIds);
        return ResultBody.ok();
    }

    /**
     * 用户分配角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @OperationLog(value = "用户分配角色")
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    @PostMapping("/user/roles/save")
    public ResultBody saveUserRoles(
            @RequestParam(value = "userId") Long userId,
            @RequestParam(value = "roleIds", required = false) String roleIds
    ) {
        systemRoleService.saveRoles(userId, roleIds != null ? StringUtils.commaDelimitedListToStringArray(roleIds) : null);
        return ResultBody.ok();
    }
}
