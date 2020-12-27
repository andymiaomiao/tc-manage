package com.weiqisen.tc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.weiqisen.tc.entity.SystemAccessLogs;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.service.SystemAccessLogsService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-08-10 16:21
 */
@Api(tags = "系统日志")
@RestController
@AllArgsConstructor
public class SystemAccessLogsController {

    private SystemAccessLogsService systemAccessLogsService;

    /**
     * 获取功能操作列表
     *
     * @param map
     * @return
     */
    @GetMapping("/access/log/page")
    public ResultBody<Page<SystemAccessLogs>> getPage(@RequestParam Map<String, Object> map) {
        return ResultBody.ok().data(systemAccessLogsService.findPage(new PageParams(map)));
    }
}
