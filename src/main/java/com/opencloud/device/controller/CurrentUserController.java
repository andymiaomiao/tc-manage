package com.opencloud.device.controller;

import com.opencloud.device.entity.SystemMenu;
import com.opencloud.device.entity.SystemUser;
import com.opencloud.device.model.CommonConstants;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.security.BaseUserDetails;
import com.opencloud.device.security.SecurityHelper;
import com.opencloud.device.service.SystemMenuService;
import com.opencloud.device.service.SystemUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author: liuyadu
 * @date: 2019/5/24 13:31
 * @description:
 */
@Api(tags = "当前登陆用户")
@RestController
public class CurrentUserController {

    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemMenuService systemMenuService;
    @Autowired
    private RedisTokenStore redisTokenStore;
    /**
     * 修改当前登录用户密码
     *
     * @return
     */
    @ApiOperation(value = "修改当前登录用户密码", notes = "修改当前登录用户密码")
    @PostMapping("/current/user/rest/password")
    public ResultBody restPassword(@RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword) {
        Boolean aBoolean = systemUserService.currentUpdatePassword(SecurityHelper.getUser().getUserId(),oldPassword, newPassword);
        if(aBoolean){
            return ResultBody.ok();
        }else{
            return ResultBody.failed().msg("旧密码输入错误，请重新输入");
        }
    }

    /**
     * 修改当前登录用户基本信息
     *
     * @param nickName
     * @param userDesc
     * @return
     */
    @ApiOperation(value = "修改当前登录用户基本信息", notes = "修改当前登录用户基本信息")
    @PostMapping("/current/user/update")
    public ResultBody updateUserInfo(
            @RequestParam(value = "nickName") String nickName,
            @RequestParam(value = "mobile") String mobile,
            @RequestParam(value = "email") String email,
            @RequestParam(value = "city") String city,
            @RequestParam(value = "userDesc", required = false) String userDesc
    ) {
        BaseUserDetails baseUserDetails = SecurityHelper.getUser();
        SystemUser user = new SystemUser();
        user.setUserId(baseUserDetails.getUserId());
        user.setNickName(nickName);
        user.setMobile(mobile);
        user.setEmail(email);
        user.setCity(city);
        user.setUserDesc(userDesc);
        systemUserService.save(user);
        baseUserDetails.setNickName(nickName);
        SecurityHelper.updateUser(redisTokenStore, baseUserDetails);
        return ResultBody.ok();
    }

    /**
     * 修改当前登录用户基本信息
     *
     * @param avatar
     * @return
     */
    @ApiOperation(value = "修改当前登录用户基本信息", notes = "修改当前登录用户基本信息")
    @PostMapping("/current/user/avatar")
    public ResultBody updateUserInfo(
            @RequestParam(value = "avatar") String avatar
    ) {
        BaseUserDetails baseUserDetails = SecurityHelper.getUser();
        SystemUser user = new SystemUser();
        user.setUserId(baseUserDetails.getUserId());
        user.setAvatar(avatar);
        systemUserService.update(user);
        baseUserDetails.setAvatar(avatar);
        SecurityHelper.updateUser(redisTokenStore, baseUserDetails);
        return ResultBody.ok();
    }

    /**
     * 获取登陆用户已分配权限
     *
     * @return
     */
    @ApiOperation(value = "获取当前登录用户已分配菜单权限", notes = "获取当前登录用户已分配菜单权限")
    @GetMapping("/current/user/menu")
    public ResultBody<List<SystemMenu>> findAuthorityMenu() {
        Map<String,Object> paramMap = new HashMap<>(16);
        paramMap.put("userId",SecurityHelper.getUser().getUserId());
        paramMap.put("userType", CommonConstants.DEVICE_MANAGE_TYPE);
        paramMap.put("roleType",CommonConstants.DEVICE_MANAGE_TYPE);
        paramMap.put("menuType",CommonConstants.DEVICE_MANAGE_TYPE);
        return ResultBody.ok().data(systemMenuService.findCustMenuByUser(paramMap));
    }

    /**
     * 获取用户基础信息
     *
     * @return
     */
    @ApiOperation(value = "获取用户基础信息")
    @GetMapping("/current/query/user")
    public ResultBody getQueryUser() {
        return ResultBody.ok().data(systemUserService.getById(SecurityHelper.getUser().getUserId()));
    }
}
