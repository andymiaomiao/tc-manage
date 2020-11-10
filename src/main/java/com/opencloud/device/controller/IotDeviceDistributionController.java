package com.opencloud.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.IotDeviceDistribution;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.service.IotDeviceDistributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-16 09:49
 */
@Api(tags = "分布地图")
@RestController
public class IotDeviceDistributionController {

    @Autowired
    private IotDeviceDistributionService iotDeviceDistributionService;

    /**
     * 分布地图分页列表
     *
     * @param map
     * @return
     */
    @GetMapping("/distribu/page")
    public ResultBody<Page<IotDeviceDistribution>> getPage(@RequestParam Map<String, Object> map) {
        return ResultBody.ok().data(iotDeviceDistributionService.findPage(new PageParams(map)));
    }

    /**
     * 分布地图列表
     *
     * @param map
     * @return
     */
    @ApiOperation(value = "获取服务列表", notes = "获取服务列表")
    @GetMapping(value = "/distribu/list")
    public ResultBody<List<IotDeviceDistribution>> getList(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(iotDeviceDistributionService.findList(new RequestParams(map)));
    }

    /**
     * 获取功能按钮详情
     *
     * @param distributionId
     * @return
     */
    @GetMapping("/distribu/info")
    public ResultBody<IotDeviceDistribution> get(@RequestParam("distributionId") Long distributionId) {
        return ResultBody.ok().data(iotDeviceDistributionService.getById(distributionId));
    }

    /**
     * 添加/修改分布地
     *
     * @return
     */
    @OperationLog(value = "添加/修改分布地")
    @ApiOperation(value = "添加/修改分布地", notes = "添加/修改分布地")
    @PostMapping("/distribu/save")
    public ResultBody<Long> save(
            @RequestParam(value = "distributionId") Long distributionId,
            @RequestParam(value = "distributionName") String distributionName,
            @RequestParam(value = "lat", defaultValue = "0.00", required = false) BigDecimal lat,
            @RequestParam(value = "lon", defaultValue = "0.00", required = false) BigDecimal lon,
            @RequestParam(value = "imgPath", required = false) String imgPath
    ) {
        IotDeviceDistribution device = new IotDeviceDistribution();
        device.setDistributionId(distributionId);
        device.setDistributionName(distributionName);
        device.setLat(lat);
        device.setLon(lon);
        device.setImgPath(imgPath);
        if(distributionId!=null){
            iotDeviceDistributionService.update(device);
        }else {
            iotDeviceDistributionService.save(device);
        }
        return ResultBody.ok();
    }

    /**
     * 移除分布地
     * @param distributionId
     * @return
     */
    @OperationLog(value = "移除分布地")
    @ApiOperation(value = "移除分布地", notes = "移除分布地")
    @PostMapping("/distribu/remove")
    ResultBody<Boolean> remove(@RequestParam("distributionId") Long distributionId){
        iotDeviceDistributionService.remove(distributionId);
        return ResultBody.ok();
    }
}
