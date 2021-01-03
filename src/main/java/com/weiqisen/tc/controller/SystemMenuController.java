package com.weiqisen.tc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiqisen.tc.entity.SystemAction;
import com.weiqisen.tc.entity.SystemMenu;
import com.weiqisen.tc.form.req.MenuApiGrantReq;
import com.weiqisen.tc.form.req.MenuIdReq;
import com.weiqisen.tc.form.req.SystemMenuSaveReq;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.mybatis.base.controller.BaseController;
import com.weiqisen.tc.service.SystemActionService;
import com.weiqisen.tc.service.SystemMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Api(tags = "系统菜单资源管理")
@RestController
public class SystemMenuController extends BaseController<SystemMenuService, SystemMenu> {
    @Autowired
    private SystemMenuService systemMenuService;
    @Autowired
    private SystemActionService systemActionService;

    /**
     * 获取分页菜单资源列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页菜单资源列表", notes = "获取分页菜单资源列表")
    @GetMapping("/menu")
    public ResultBody<Page<SystemMenu>> getPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(bizService.findPage(new PageParams(map)));
    }

    /**
     * 菜单所有资源列表
     *
     * @return
     */
    @ApiOperation(value = "菜单所有资源列表", notes = "菜单所有资源列表")
    @GetMapping("/menu/list")
    public ResultBody<List<SystemMenu>> getList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(bizService.findList(new PageParams(map)));
    }


    /**
     * 获取菜单下所有操作
     *
     * @param menuId
     * @return
     */
    @ApiOperation(value = "获取菜单下所有操作", notes = "获取菜单下所有操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", value = "menuId", paramType = "form"),
    })
    @GetMapping("/menu/action")
    public ResultBody<List<SystemAction>> getMenuAction(Long menuId) {
        return ResultBody.ok().data(systemActionService.findListByMenuId(menuId));
    }

    /**
     * 获取菜单资源详情
     *
     * @param menuId
     * @return 应用信息
     */
    @ApiOperation(value = "获取菜单资源详情", notes = "获取菜单资源详情")
    @GetMapping("/menu/info")
    public ResultBody<SystemMenu> get(@RequestParam("menuId") Long menuId) {
        return ResultBody.ok().data(bizService.getById(menuId));
    }

    /**
     * 添加/编辑菜单资源
     *
     * @return
     */
    @ApiOperation(value = "添加/编辑菜单资源", notes = "添加/编辑菜单资源")
    @PostMapping("/menu/save")
    public ResultBody save(
            @RequestBody SystemMenuSaveReq systemMenuSaveReq
    ) {
        SystemMenu menu = new SystemMenu();
        BeanUtils.copyProperties(systemMenuSaveReq, menu);
        if (menu.getMenuId() == null) {
            bizService.add(menu);
        } else {
            bizService.update(menu);
        }
        return ResultBody.ok();
    }

    /**
     * 移除菜单资源
     *
     * @param menuIdReq
     * @return
     */
    @ApiOperation(value = "移除菜单资源", notes = "移除菜单资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "menuId", required = true, value = "menuId", paramType = "form"),
    })
    @PostMapping("/menu/remove")
    public ResultBody<Boolean> remove(
            @RequestBody MenuIdReq menuIdReq
    ) {
        bizService.remove(menuIdReq.getMenuId());
        return ResultBody.ok();
    }

    /**
     * 功能按钮绑定API
     *
     * @return
     */
    @ApiOperation(value = "功能按钮授权", notes = "功能按钮授权")
    @PostMapping("/menu/api/grant")
    public ResultBody grantAuthorityAction(
            @RequestBody MenuApiGrantReq menuApiGrantReq
    ) {
        bizService.addMenuApi(menuApiGrantReq.getMenuId(), menuApiGrantReq.getApiIds() != null ? StringUtils.commaDelimitedListToStringArray(menuApiGrantReq.getApiIds()) : null);
        return ResultBody.ok();
    }

}
