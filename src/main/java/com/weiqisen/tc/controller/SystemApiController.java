package com.weiqisen.tc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;

import com.weiqisen.tc.entity.SystemApi;
import com.weiqisen.tc.form.req.*;
import com.weiqisen.tc.model.CommonConstants;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.model.ResultBody;
import com.weiqisen.tc.mybatis.base.controller.BaseController;
import com.weiqisen.tc.service.SystemApiService;
import com.weiqisen.tc.utils.MD5Util;
import com.weiqisen.tc.utils.RestUtil;
import com.weiqisen.tc.utils.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Api(tags = "系统接口资源管理")
@RestController
public class SystemApiController extends BaseController<SystemApiService, SystemApi> {

    @Autowired
    private SystemApiService systemApiService;


    @Autowired
    private RestUtil restUtil;

    @Autowired
    WebApplicationContext applicationContext;


    /**
     * 获取分页接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取分页接口列表", notes = "获取分页接口列表")
    @GetMapping(value = "/api/page")
    public ResultBody<Page<SystemApi>> getPage(@RequestParam(required = false) Map map) {
        return ResultBody.ok().data(bizService.findPage(new PageParams(map)));
    }

    

    @GetMapping(value = "/fgdgfd")
    public ResultBody<String> getTestPage(@RequestParam(required = false) Map map) {
        String a = "Hello";
        return ResultBody.ok().data(a);
    }
    /**
     * 获取所有接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有接口列表", notes = "获取所有接口列表")
    @GetMapping("/api/list")
    public ResultBody<List<SystemApi>> getList(@RequestParam(value = "serviceId", required = false) String serviceId) {
        return ResultBody.ok().data(bizService.findList(serviceId));
    }

    /**
     * 获取所有接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取所有接口列表", notes = "获取所有接口列表")
    @GetMapping("/api/action/list")
    public ResultBody<Map> getActionList(@RequestParam(value = "actionId", required = false) String actionId, @RequestParam(value = "actionType") Integer actionType) {
        return ResultBody.ok().data(bizService.findActionList(actionId, actionType));
    }

    /**
     * 获取权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取接口权限列表", notes = "获取接口权限列表")
    @GetMapping("/api/menu/list")
    public ResultBody<Map> findAuthorityApiMenu(
            @RequestParam(value = "apiType", required = false) Integer apiType,
            @RequestParam(value = "menuId", required = false) Long menuId
    ) {
        return ResultBody.ok().data(bizService.findMenuApiList(menuId, apiType));
    }

    /**
     * 获取应用接口列表
     *
     * @return
     */
    @ApiOperation(value = "获取应用接口列表", notes = "获取应用接口列表")
    @GetMapping("/api/client/list")
    public ResultBody<Map> getClientList(@RequestParam(value = "clientId", required = false) String clientId, @RequestParam(value = "actionType") Integer actionType) {
        return ResultBody.ok().data(bizService.findClientList(clientId, actionType));
    }

    /**
     * 获取接口资源
     *
     * @param apiId
     * @return
     */
    @ApiOperation(value = "获取接口资源", notes = "获取接口资源")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "apiId", required = true, value = "ApiId", paramType = "path"),
    })
    @GetMapping("/api/info")
    public ResultBody<SystemApi> getApi(@RequestParam("apiId") Long apiId) {

        return ResultBody.ok().data(bizService.getById(apiId));
    }

    /**
     * 编辑接口资源
     *
     * @return
     */
    @ApiOperation(value = "编辑接口资源", notes = "编辑接口资源")
    @PostMapping("/api/save")
    public ResultBody save(
            @RequestBody MenuSaveReq menuSaveReq
    ) {
        SystemApi api = new SystemApi();
        BeanUtils.copyProperties(menuSaveReq, api);
        api.setApiType(10);
        if (api.getApiId() == null) {
            bizService.add(api);
        } else {
            bizService.update(api);
        }
        return ResultBody.ok();
    }

    /**
     * 移除接口资源
     *
     * @return
     */
    @ApiOperation(value = "移除接口资源", notes = "移除接口资源")
    @PostMapping("/api/remove")
    public ResultBody remove(
            @RequestBody ApiRemoveReq apiRemoveReq
    ) {
        bizService.remove(apiRemoveReq.getApiId());
        return ResultBody.ok();
    }

    /**
     * 批量删除数据
     *
     * @return
     */
    @ApiOperation(value = "批量删除数据", notes = "批量删除数据")
    @PostMapping("/api/batch/remove")
    public ResultBody batchRemove(
            @RequestBody ApiBatchRemoveReq apiBatchRemoveReq
    ) {
        QueryWrapper<SystemApi> wrapper = new QueryWrapper();
        wrapper.lambda().in(SystemApi::getApiId, apiBatchRemoveReq.getIds().split(",")).eq(SystemApi::getIsPersist, 0);
        bizService.remove(wrapper);
        return ResultBody.ok();
    }

    /**
     * 批量修改公开状态
     *
     * @return
     */
    @ApiOperation(value = "批量修改", notes = "批量修改")
    @PostMapping("/api/batch/update")
    public ResultBody batchUpdate(
            @RequestBody ApiBatchUpdateReq apiBatchUpdateReq
    ) {
        QueryWrapper<SystemApi> wrapper = new QueryWrapper();
        wrapper.lambda().in(SystemApi::getApiId, StringUtils.commaDelimitedListToSet(apiBatchUpdateReq.getIds()));
        SystemApi entity = new SystemApi();
        if ("status".equals(apiBatchUpdateReq.getType())) {
            entity.setStatus(apiBatchUpdateReq.getValue());
        } else if ("auth".equals(apiBatchUpdateReq.getType())) {
            entity.setIsAuth(apiBatchUpdateReq.getValue());
        } else {
            return ResultBody.ok();
        }
        bizService.update(entity, wrapper);
        return ResultBody.ok();
    }

    /**
     * 功能按钮绑定API
     *

     * @return
     */
    @ApiOperation(value = "功能按钮授权", notes = "功能按钮授权")
    @PostMapping("/api/action/grant")
    public ResultBody grantAuthorityAction(
        @RequestBody ApiActionGrantReq apiActionGrantReq
    ) {
        bizService.addApiAction(apiActionGrantReq.getActionId(), apiActionGrantReq.getApiIds() != null ? StringUtils.commaDelimitedListToStringArray(apiActionGrantReq.getApiIds()) : null);
        return ResultBody.ok();
    }

//    @ApiOperation(value = "获取批量更新的服务器列表", notes = "获取批量更新的服务器列表")
//    @GetMapping("/api/batch/update/server/list")
//    public ResultBody batchUpdate(@RequestParam(value = "serviceId") String serviceId, HttpServletRequest request) {
//        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
//        if(instances!=null&&instances.size()>0) {
//            ServiceInstance instance = instances.get(0);
//            String url = "http://" + instance.getHost() + ":" + instance.getPort() + "/api/resource";
//            Map<String, String> postParameters = new LinkedHashMap<>();
//            HttpHeaders headers = new HttpHeaders();
//
//            headers.set("Authorization", request.getHeader("Authorization"));
//            ResultBody get = restUtil.get(url, headers.toSingleValueMap(), postParameters, ResultBody.class);
//            return get;
//        }else {
//            throw new BaseException("服务未找到");
//        }
//    }

    @ApiOperation(value = "批量添加资源服务器", notes = "批量更新资源服务器")
    @PostMapping("/api/batch/add/server")
    public ResultBody batchUpdateServiceApi(@RequestBody SystemApiBatchUpdateReq systemApiBatchUpdateReq) {
        bizService.batchUpdateServiceApi(systemApiBatchUpdateReq);
        return ResultBody.ok();
    }

    @ApiOperation(value = "批量更新资源服务器", notes = "批量更新资源服务器")
    @PostMapping("/api/batch/update/server")
    public ResultBody batchUpdateService(
            @RequestParam(value = "serviceIds") String serviceIds
    ) {
        if (bizService.batchUpdateService(StringUtils.commaDelimitedListToStringArray(serviceIds))) {
        }
        return ResultBody.ok();
    }


    @ApiOperation(value = "获取批量更新的服务器列表", notes = "获取批量更新的服务器列表")
    @GetMapping("/api/resource")
    public ResultBody apiResource() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        // 获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        List<Map<String, String>> list = new ArrayList();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();
            Map<String, String> api = Maps.newHashMap();
            String methodName = method.getMethod().getName();
            // 类名
            String className = method.getMethod().getDeclaringClass().getName();
            if (!className.contains("com.opencloud")) {
                continue;
            }
            PatternsRequestCondition p = info.getPatternsCondition();
            for (String url : p.getPatterns()) {
                api.put("path", url);
            }
            String md5 = MD5Util.Md5CodeEncode(CommonConstants.ADMIN_MANAGE_TYPE + api.get("path") + methodName);
            String name = "";
            String desc = "";
            ApiOperation apiOperation = method.getMethodAnnotation(ApiOperation.class);
            if (apiOperation != null) {
                name = apiOperation.value();
                desc = apiOperation.notes();
            }
            name = StringUtil.isBlank(name) ? methodName : name;
            api.put("apiId", IdWorker.getId() + "");
            api.put("apiName", name);
            api.put("apiType", CommonConstants.ADMIN_MANAGE_TYPE + "");
            api.put("apiCode", md5);
            api.put("apiDesc", desc);
            api.put("className", className);
            api.put("methodName", methodName);
            api.put("md5", md5);
            api.put("serviceId", CommonConstants.ADMIN_MANAGE_NAME);
            api.put("schemas", "/admin");
//            api.put("contentType", mediaTypes);
            api.put("isAuth", "1");
            RequestMethodsRequestCondition methodsCondition = info.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                api.put("requestMethod", requestMethod.toString());
            }
            list.add(api);
        }
        return ResultBody.ok().data(list);
    }
}
