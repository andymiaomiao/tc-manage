package com.opencloud.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.SystemRole;
import com.opencloud.device.entity.SystemRoleUser;
import com.opencloud.device.model.CommonConstants;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.service.SystemRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
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
@Api(tags = "角色中心")
@RestController
@AllArgsConstructor
public class SystemRoleController {

    private SystemRoleService systemRoleService;

    /**
     * 角色分页用户列表
     *
     * @return
     */
    @ApiOperation(value = "系统分页用户列表", notes = "系统分页用户列表")
    @GetMapping("/role/page")
    public ResultBody<Page<SystemRole>> getPage(@RequestParam(required = false) Map map) {
        map.put("roleType", CommonConstants.DEVICE_MANAGE_TYPE);
        return ResultBody.ok().data(systemRoleService.findPage(new PageParams(map)));
    }

    /**
     * 获取所有角色列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有角色列表", notes = "获取所有角色列表")
    @GetMapping("/role/list")
    public ResultBody<List<SystemRole>> getList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(systemRoleService.findList(new RequestParams(map)));
    }

    /**
     * 获取角色详情
     *
     * @param roleId
     * @return
     */
    @OperationLog(value = "删除角色")
    @ApiOperation(value = "获取角色详情", notes = "获取角色详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "path")
    })
    @GetMapping("/role/info")
    public ResultBody<SystemRole> get(@RequestParam(value = "roleId") Long roleId) {
        return ResultBody.ok().data(systemRoleService.getById(roleId));
    }

    /**
     * 添加/编辑角色
     *
     * @param roleId   角色ID
     * @param roleCode 角色编码
     * @param roleName 角色显示名称
     * @param roleDesc 描述
     * @param status   启用禁用
     * @return
     */
    @OperationLog(value = "添加/编辑角色")
    @ApiOperation(value = "添加/编辑角色", notes = "添加/编辑角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", paramType = "form"),
            @ApiImplicitParam(name = "roleCode", value = "角色编码", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleName", value = "角色显示名称", defaultValue = "", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleType", value = "角色平台类型", defaultValue = "10", required = true, paramType = "form"),
            @ApiImplicitParam(name = "roleDesc", value = "描述", defaultValue = "", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form")
    })
    @PostMapping("/role/save")
    public ResultBody save(
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "roleCode") String roleCode,
            @RequestParam(value = "roleName") String roleName,
            @RequestParam(value = "roleType",defaultValue = "10") Integer roleType,
            @RequestParam(value = "roleDesc", required = false) String roleDesc,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        SystemRole role = new SystemRole();
        role.setRoleId(roleId);
        role.setRoleCode(roleCode);
        role.setRoleName(roleName);
        role.setRoleType(roleType);
        role.setStatus(status);
        role.setRoleDesc(roleDesc);
        SystemRole add = null;
        if(roleId!=null){
            add = systemRoleService.update(role);
        }else {
            add = systemRoleService.add(role);
        }
        return ResultBody.ok().data(add.getRoleId());
    }


    /**
     * 删除角色
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", defaultValue = "", required = true, paramType = "form")
    })
    @PostMapping("/role/remove")
    public ResultBody remove(
            @RequestParam(value = "roleId") Long roleId
    ) {
        systemRoleService.remove(roleId);
        return ResultBody.ok();
    }

    /**
     * 角色添加成员
     *
     * @param roleId
     * @param userIds
     * @return
     */
    @ApiOperation(value = "角色添加成员", notes = "角色添加成员")
    @PostMapping("/role/users/save")
    public ResultBody saveUsers(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "userIds", required = false) String userIds
    ) {
        systemRoleService.saveUsers(roleId, userIds);
        return ResultBody.ok();
    }

    /**
     * 查询角色成员
     *
     * @param roleId
     * @return
     */
    @ApiOperation(value = "查询角色成员", notes = "查询角色成员")
    @GetMapping("/role/users")
    public ResultBody<List<SystemRoleUser>> getRoleUsers(
            @RequestParam(value = "roleId") Long roleId
    ) {
        return ResultBody.ok().data(systemRoleService.findUsersByRoleId(roleId));
    }

    /**
     * 租户商户功能授权
     *
     * @param roleType
     * @return
     */
    @ApiOperation(value = "功能按钮授权", notes = "功能按钮授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", required = true, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "roleType", required = true, value = "功能按钮ID", paramType = "form")
    })
    @GetMapping("/role/authority/menu")
    public ResultBody<Map<String, Object>> authorityRoleMenu(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "roleType") Integer roleType

    ) {
        return ResultBody.ok().data(systemRoleService.authorityCustRoleMenu(roleId,roleType));
    }

    /**
     * 分配角色权限
     *
     * @param roleId       角色ID
     * @param menuIds 权限ID.多个以,隔开
     * @return
     */
    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, paramType = "form"),
            @ApiImplicitParam(name = "menuIds", value = "权限ID.多个以,隔开.选填", required = true, paramType = "form")
    })
    @PostMapping("role/grant/menu")
    public ResultBody grantRoleMenu(
            @RequestParam(value = "roleId") Long roleId,
            @RequestParam(value = "menuIds") String menuIds
    ) {
        systemRoleService.grantRoleMenu(roleId, menuIds);
        return ResultBody.ok();
    }
}
