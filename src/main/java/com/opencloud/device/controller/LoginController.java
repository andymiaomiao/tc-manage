package com.opencloud.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.opencloud.device.annotation.OperationLog;
import com.opencloud.device.model.CommonConstants;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.security.ServerConfiguration;
import com.opencloud.device.security.SocialClientDetails;
import com.opencloud.device.security.SocialProperties;
import com.opencloud.device.utils.RestUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: liuyadu
 * @date: 2018/11/9 15:43
 * @description:
 */
@Api(tags = "用户认证中心")
@RestController
public class LoginController {

    @Autowired
    private SocialProperties socialProperties;
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RestUtil restUtil;
    @Autowired
    private ServerConfiguration serverConfiguration;


    /**
     * 获取用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取用户基础信息")
    @GetMapping("/current/user")
    public ResultBody getUserProfile() {
        return ResultBody.ok().data(SecurityHelper.getUser());
    }



    /**
     * 获取用户访问令牌
     * 基于oauth2密码模式登录
     *
     * @param username
     * @param password
     * @return access_token
     */
    @OperationLog(value = "用户登陆")
    @ApiOperation(value = "用户登陆", notes = "基于oauth2密码模式登录,无需签名,返回access_token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", required = true, value = "登录名", paramType = "form"),
            @ApiImplicitParam(name = "password", required = true, value = "登录密码", paramType = "form")
    })
    @PostMapping("/login/token")
    public Object getLoginToken(@RequestParam String username, @RequestParam String password,@RequestParam(required = false) String type, @RequestHeader HttpHeaders httpHeaders) throws Exception {
        JSONObject result = getToken(username, password, type, httpHeaders);
        if (result.containsKey("access_token")) {
            return ResultBody.ok().data(result);
        } else {
            return result;
        }
    }


    /**
     * 退出移除令牌
     *
     * @param token
     */
    @OperationLog(value = "用户退出")
    @ApiOperation(value = "用户退出", notes = "退出移除令牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", required = true, value = "访问令牌", paramType = "form")
    })
    @PostMapping("/logout/token")
    public ResultBody removeToken(@RequestParam String token) {
        tokenStore.removeAccessToken(tokenStore.readAccessToken(token));
        return ResultBody.ok();
    }


    /**
     * 生成 oauth2 token
     *
     * @param userName
     * @param password
     * @param type
     * @return
     */
    public JSONObject getToken(String userName, String password, String type, HttpHeaders headers) {
        SocialClientDetails clientDetails = socialProperties.getClient().get("device");
        String url = serverConfiguration.getUrl() + "/oauth/token";
        // 使用oauth2密码模式登录.
        Map<String, String> postParameters = new LinkedHashMap<>();
        postParameters.put("username", userName);
        postParameters.put("password", password);
        postParameters.put("thirdParty", type);
        postParameters.put("client_id", clientDetails.getClientId());
        postParameters.put("client_secret", clientDetails.getClientSecret());
        postParameters.put("grant_type", "password");
        // 添加参数区分,第三方登录
        postParameters.put("loginType", CommonConstants.DEVICE_MANAGE_TYPE+"");
        // 使用客户端的请求头,发起请求
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 强制移除 原来的请求头,防止token失效
        headers.remove(HttpHeaders.AUTHORIZATION);
        JSONObject result = restUtil.post(url, headers.toSingleValueMap(), postParameters, JSONObject.class);
        return result;
    }

}
