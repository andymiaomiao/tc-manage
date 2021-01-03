package com.weiqisen.tc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiqisen.tc.entity.SystemAction;
import com.weiqisen.tc.form.req.ApiActionGrantReq;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.mybatis.base.controller.BaseController;
import com.weiqisen.tc.service.SystemActionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author liuyadu
 */
@Api(tags = "系统功能按钮管理")
@RestController
public class SystemActionController extends BaseController<SystemActionService, SystemAction> {
    @Autowired
    private SystemActionService systemActionService;

    /**
     * 获取分页功能按钮列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页功能按钮列表", notes = "获取分页功能按钮列表")
    @GetMapping("/action/page")
    public ResultBody<Page<SystemAction>> findPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(bizService.findPage(new PageParams(map)));
    }


    /**
     * 获取功能按钮详情
     *
     * @param actionId
     * @return
     */
    @ApiOperation(value = "获取功能按钮详情", notes = "获取功能按钮详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮Id", paramType = "path"),
    })
    @GetMapping("/action/info")
    public ResultBody<SystemAction> get(@RequestParam("actionId") Long actionId) {
        return ResultBody.ok().data(bizService.getById(actionId));
    }

    /**
     * 添加/编辑功能按钮
     *
     * @param actionId   功能按钮ID
     * @param actionCode 功能按钮编码
     * @param actionName 功能按钮名称
     * @param menuId     上级菜单
     * @param status     是否启用
     * @param priority   优先级越小越靠前
     * @param actionDesc 描述
     * @return
     */
    @ApiOperation(value = "添加/编辑功能按钮", notes = "添加/编辑功能按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = false, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "actionCode", required = true, value = "功能按钮编码", paramType = "form"),
            @ApiImplicitParam(name = "actionName", required = true, value = "功能按钮名称", paramType = "form"),
            @ApiImplicitParam(name = "actionPath", required = false, value = "功能按钮路径", paramType = "form"),
            @ApiImplicitParam(name = "menuId", required = true, value = "上级菜单", paramType = "form"),
            @ApiImplicitParam(name = "status", required = true, defaultValue = "1", allowableValues = "0,1", value = "是否启用", paramType = "form"),
            @ApiImplicitParam(name = "priority", required = false, value = "优先级越小越靠前", paramType = "form"),
            @ApiImplicitParam(name = "actionDesc", required = false, value = "描述", paramType = "form"),
    })
    @PostMapping("/action/save")
    public ResultBody save(
            @RequestParam(value = "actionId", required = false) Long actionId,
            @RequestParam(value = "actionCode") String actionCode,
            @RequestParam(value = "actionName") String actionName,
            @RequestParam(value = "actionPath", required = false) String actionPath,
            @RequestParam(value = "menuId") Long menuId,
            @RequestParam(value = "status", defaultValue = "1") Integer status,
            @RequestParam(value = "priority", required = false, defaultValue = "0") Integer priority,
            @RequestParam(value = "actionDesc", required = false, defaultValue = "") String actionDesc
    ) {
        SystemAction action = new SystemAction();
        action.setActionId(actionId);
        action.setActionCode(actionCode);
        action.setActionName(actionName);
        action.setActionPath(actionPath);
        action.setMenuId(menuId);
        action.setStatus(status);
        action.setPriority(priority);
        action.setActionDesc(actionDesc);
        if (action.getActionId() == null) {
            bizService.add(action);
        } else {
            bizService.update(action);
        }
        return ResultBody.ok();    }


    /**
     * 移除功能按钮
     *
     * @return
     */
    @ApiOperation(value = "移除功能按钮", notes = "移除功能按钮")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form")
    })
    @PostMapping("/action/remove")
    public ResultBody removeAction(
            @RequestBody ApiActionGrantReq apiActionGrantReq
    ) {
        bizService.remove(apiActionGrantReq.getActionId());
        // 刷新网关
        return ResultBody.ok();    }

    /**
     * 功能按钮绑定API
     *
     * @return
     */
    @ApiOperation(value = "功能按钮授权", notes = "功能按钮授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actionId", required = true, value = "功能按钮ID", paramType = "form"),
            @ApiImplicitParam(name = "apiIds", required = false, value = "全新ID:多个用,号隔开", paramType = "form"),
    })
    @PostMapping("/action/api/grant")
    public ResultBody grantAuthorityAction(
            @RequestBody ApiActionGrantReq apiActionGrantReq
    ) {
        bizService.addActionApi(apiActionGrantReq.getActionId(), apiActionGrantReq.getApiIds() != null ? StringUtils.commaDelimitedListToStringArray(apiActionGrantReq.getApiIds()) : null);
        return ResultBody.ok();    }
}
