package com.weiqisen.tc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiqisen.tc.annotation.OperationLog;
import com.weiqisen.tc.entity.SystemRole;
import com.weiqisen.tc.entity.SystemRoleUser;
import com.weiqisen.tc.form.req.SystemRemoveRoleReq;
import com.weiqisen.tc.form.req.SystemRoleGrantReq;
import com.weiqisen.tc.form.req.SystemRoleReq;
import com.weiqisen.tc.form.req.SystemRoleUserReq;
import com.weiqisen.tc.model.CommonConstants;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.RequestParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.security.SecurityHelper;
import com.weiqisen.tc.service.SystemRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
     * @return
     */
    @OperationLog(value = "添加/编辑角色")
    @ApiOperation(value = "添加/编辑角色", notes = "添加/编辑角色")
    @PostMapping("/role/save")
    public ResultBody save(
            @RequestBody SystemRoleReq systemRoleReq
    ) {
        SystemRole role = new SystemRole();
        BeanUtils.copyProperties(systemRoleReq, role);
        role.setTenantId(SecurityHelper.getUser().getTenantId());
        SystemRole add = null;
        if(role.getRoleId()!=null){
            add = systemRoleService.update(role);
        }else {
            add = systemRoleService.add(role);
        }
        return ResultBody.ok().data(add.getRoleId());
    }


    /**
     * 删除角色
     *
     * @return
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @PostMapping("/role/remove")
    public ResultBody remove(
            @RequestBody SystemRemoveRoleReq systemRemoveRoleReq
    ) {
        systemRoleService.remove(systemRemoveRoleReq.getRoleId());
        return ResultBody.ok();
    }

    /**
     * 角色添加成员
     *
     * @return
     */
    @ApiOperation(value = "角色添加成员", notes = "角色添加成员")
    @PostMapping("/role/users/save")
    public ResultBody saveUsers(
            @RequestBody SystemRoleUserReq systemRoleUserReq
    ) {
        systemRoleService.saveUsers(systemRoleUserReq.getRoleId(), systemRoleUserReq.getUserIds());
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
     * @return
     */
    @ApiOperation(value = "分配角色权限", notes = "分配角色权限")
    @PostMapping("role/grant/menu")
    public ResultBody grantRoleMenu(
            @RequestBody SystemRoleGrantReq systemRoleGrantReq
    ) {
        systemRoleService.grantRoleMenu(systemRoleGrantReq.getRoleId(), systemRoleGrantReq.getMenuIds());
        return ResultBody.ok();
    }
}
