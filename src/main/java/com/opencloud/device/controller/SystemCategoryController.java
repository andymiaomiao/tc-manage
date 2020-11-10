package com.opencloud.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.entity.SystemCategory;
import com.opencloud.device.model.CommonConstants;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.service.IotDeviceService;
import com.opencloud.device.service.SystemCategoryService;
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
 * @author liuyadu
 */
@Api(tags = "分类管理")
@RestController
@AllArgsConstructor
public class SystemCategoryController {

    private SystemCategoryService systemCategoryService;


    private IotDeviceService iotDeviceService;

    /**
     * 分类分页列表
     *
     * @return
     */
    @ApiOperation(value = "分类分页列表", notes = "分类分页列表")
    @GetMapping("/category/page")
    public ResultBody<Page<SystemCategory>> getPage(@RequestParam(required = false) Map map) {
        map.put("categoryType", CommonConstants.DEVICE_MANAGE_TYPE);
        return ResultBody.ok().data(systemCategoryService.findPage(new PageParams(map)));
    }

    /**
     * 获取功能操作列表
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "获取功能操作列表", notes = "获取功能操作列表")
    @GetMapping("/category/list")
    public ResultBody<List<SystemCategory>> getList(@RequestParam Map<String, Object> map) {
        map.put("categoryType", CommonConstants.DEVICE_MANAGE_TYPE);
        return ResultBody.ok().data(systemCategoryService.findList(new RequestParams(map)));
    }

    /**
     * 添加/编辑分类
     *
     * @param categoryId 分类ID
     * @param parentId 父id
     * @param level 层级
     * @param name 名称
     * @param sort 描述
     * @return
     */
    @OperationLog(value = "添加/编辑分类")
    @ApiOperation(value = "添加/编辑分类", notes = "添加/编辑分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类ID", paramType = "form"),
            @ApiImplicitParam(name = "parentId", value = "父id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "level", value = "层级", defaultValue = "0", required = true, paramType = "form"),
            @ApiImplicitParam(name = "name", value = "名称", required = true,paramType = "form"),
            @ApiImplicitParam(name = "sort", value = "描述",defaultValue = "0", paramType = "form"),
            @ApiImplicitParam(name = "status", value = "状态",defaultValue = "1", paramType = "form")
    })
    @PostMapping("/category/save")
    public ResultBody<Long> save(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "parentId") Long parentId,
            @RequestParam(value = "level",defaultValue = "0") Integer level,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "sort", defaultValue = "0", required = false) Integer sort,
            @RequestParam(value = "status", defaultValue = "1", required = false) Integer status
    ) {
        SystemCategory systemCategory = new SystemCategory();
        systemCategory.setCategoryId(categoryId);
        systemCategory.setParentId(parentId);
        systemCategory.setLevel(level);
        systemCategory.setName(name);
        systemCategory.setStatus(status);
        systemCategory.setSort(sort);
        systemCategory.setCategoryType(CommonConstants.DEVICE_MANAGE_TYPE);
        if(categoryId!=null){
            systemCategoryService.update(systemCategory);
        }else {
            systemCategoryService.add(systemCategory);
        }
        return ResultBody.ok();
    }

    /**
     * 移除分类
     *
     * @param categoryId
     * @return
     */
    @OperationLog(value = "删除分类")
    @ApiOperation(value = "删除分类", notes = "删除分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryId", value = "分类ID", required = true, paramType = "form")
    })
    @PostMapping("/category/remove")
    public ResultBody remove(@RequestParam("categoryId") Long categoryId) {
        Map<String,Object> requestMap = Maps.newHashMap();
        requestMap.put("categoryId",categoryId);
        List<IotDevice> iotDeviceBody = iotDeviceService.findList(new RequestParams(requestMap));
        if(iotDeviceBody!=null&&iotDeviceBody.size()>0){
            return ResultBody.failed().msg("有设备使用该分类，请解绑该分类后再操作！");
        }
        systemCategoryService.remove(categoryId);
        return ResultBody.ok();
    }
}
