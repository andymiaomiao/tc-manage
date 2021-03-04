package com.weiqisen.tc.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.weiqisen.tc.entity.TcDevice;
import com.weiqisen.tc.form.req.TcDeviceSaveReq;
import com.weiqisen.tc.form.res.TcDeviceRes;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.security.SecurityHelper;
import com.weiqisen.tc.service.TcDeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.util.Map;


/**
 * 设备管理
 *
 * @author weiqisen
 * @date 2020-11-30
 */
@Api(value = "设备管理", tags = "设备管理")
@RestController
public class TcDeviceController {

    @Autowired
    private TcDeviceService tcDeviceService;

    /**
     * 获取设备管理页
     *
     * @param map
     * @return
     */
    @GetMapping("/device/manage/page")
    public ResultBody<Page<TcDeviceRes>> getPage(@RequestParam Map<String, Object> map) {
        Long userId = SecurityHelper.getUserId();
        map.put("userId", userId);
        return ResultBody.ok().data(tcDeviceService.findPage(new PageParams(map)));
    }

    @GetMapping("/hello")
    public ResultBody hello() {
        return ResultBody.ok().data("hello Docker!");
    }


    /**
     * 获取设备管理详情
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "获取设备管理详情", notes = "获取设备管理详情")
    @GetMapping("/device/manage/info")
    public ResultBody<TcDeviceRes> get(@RequestParam("deviceId") Long deviceId) {
        return ResultBody.ok().data(tcDeviceService.getById(deviceId));
    }

    /**
     * 编辑/保存设备信息
     *
     * @param tcDeviceSaveReq
     * @return
     */
    @ApiOperation(value = "修改设备信息", notes = "修改设备信息")
    @PostMapping("/device/manage/save")
    public ResultBody save(@RequestBody TcDeviceSaveReq tcDeviceSaveReq) {
        TcDevice tcDevice = new TcDevice();
        BeanUtils.copyProperties(tcDeviceSaveReq, tcDevice);
        return ResultBody.ok().data(tcDeviceService.save(tcDevice));
    }

    /**
     * 删除设备
     *
     * @param deviceId
     * @return
     */
    @ApiOperation(value = "删除设备信息", notes = "删除设备信息")
    @DeleteMapping("/device/manage/remove")
    public ResultBody remove(@RequestParam("deviceId") Long deviceId) {
        return ResultBody.ok().data(tcDeviceService.removeById(deviceId));
    }
}
