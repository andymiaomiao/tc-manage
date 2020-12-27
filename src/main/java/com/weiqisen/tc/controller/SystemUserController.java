package com.weiqisen.tc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiqisen.tc.annotation.OperationLog;
import com.weiqisen.tc.entity.SystemUser;
import com.weiqisen.tc.form.req.SystemRemoveUserReq;
import com.weiqisen.tc.form.req.SystemUserInfoReq;
import com.weiqisen.tc.form.req.SystemUserRoleSaveReq;
import com.weiqisen.tc.form.req.UserReq;
import com.weiqisen.tc.model.CommonConstants;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.service.SystemRoleService;
import com.weiqisen.tc.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author weiqisen
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
     * @return
     */
    @OperationLog(value = "添加/编辑用户")
    @ApiOperation(value = "添加/编辑用户", notes = "添加/编辑用户")
    @PostMapping("/user/save")
    public ResultBody save(
            @RequestBody SystemUserInfoReq systemUserInfoReq
    ) {
        SystemUser user = new SystemUser();
        BeanUtils.copyProperties(systemUserInfoReq, user);
        user.setType(CommonConstants.DEVICE_MANAGE_TYPE);
        if(systemUserInfoReq.getUserId()!=null){
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
    public ResultBody remove(@RequestBody SystemRemoveUserReq systemRemoveUserReq){
        systemUserService.remove(systemRemoveUserReq.getUserId());
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
     * @return
     */
    @OperationLog(value = "用户分配角色")
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    @PostMapping("/user/roles/save")
    public ResultBody saveUserRoles(
            @RequestBody SystemUserRoleSaveReq systemUserRoleSaveReq
    ) {
        systemRoleService.saveRoles(systemUserRoleSaveReq.getUserId(), systemUserRoleSaveReq.getRoleIds());
        return ResultBody.ok();
    }
}
