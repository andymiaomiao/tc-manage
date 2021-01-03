package com.weiqisen.tc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiqisen.tc.entity.SystemDict;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.RequestParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.mybatis.base.controller.BaseController;
import com.weiqisen.tc.service.SystemDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-28 15:06
 */
@Api(tags = "服务管理")
@RestController
public class SystemDictController extends BaseController<SystemDictService, SystemDict> {

    /**
     * 获取分页接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping(value = "/dict/page")
    public ResultBody<Page<SystemDict>> getPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(bizService.findPage(new PageParams(map)));
    }


    @ApiOperation(value = "获取服务列表", notes = "获取服务列表")
    @GetMapping(value = "/dict/list")
    public ResultBody<List<SystemDict>> getServiceList(@RequestParam(required = false) Map map) {
        RequestParams requestParams = new RequestParams(map);
        SystemDict query = requestParams.mapToBean(SystemDict.class);
        return ResultBody.ok().data(bizService.getServiceList(query));
    }

    /**
     * 添加/编辑功能按钮
     *
     * @return
     */
    @ApiOperation(value = "添加/编辑功能按钮", notes = "添加/编辑功能按钮")
    @PostMapping("/dict/save")
    public ResultBody save(
            @RequestBody SystemDict systemDict
    ) {

        SystemDict dict = new SystemDict();
        BeanUtils.copyProperties(systemDict, dict);
        dict.setParentId(0L);
        if (dict.getDictId() == null) {
            bizService.add(dict);
        } else {
            bizService.update(dict);
        }
        return ResultBody.ok();
    }
}
