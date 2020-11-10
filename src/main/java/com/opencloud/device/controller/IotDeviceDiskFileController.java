package com.opencloud.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.entity.DiskFile;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.service.DiskFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-01 14:58
 */
@Api(tags = "升级文件")
@RestController
@AllArgsConstructor
public class IotDeviceDiskFileController {

    private DiskFileService diskFileService;
    /**
     * 获取文件分页列表
     *
     * @param map
     * @return
     */
    @GetMapping("/disk/file/page")
    public ResultBody<Page<DiskFile>> getPage(@RequestParam Map<String, Object> map) {
        return ResultBody.ok().data(diskFileService.findPage(new PageParams(map)));
    }

    /**
     * 移除功能按钮
     *
     * @param fileId
     * @return
     */
    @OperationLog(value = "移除升级文件")
    @ApiOperation(value = "移除升级文件", notes = "移除升级文件")
    @PostMapping("/disk/file/remove")
    public ResultBody remove(@RequestParam("fileId") Long fileId) {
        diskFileService.remove(fileId);
        return ResultBody.ok();
    }
}
