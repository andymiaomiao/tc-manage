package com.weiqisen.tc.form.res;

import com.google.common.collect.Lists;
import com.weiqisen.tc.entity.SystemAccount;
import com.weiqisen.tc.security.BaseAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @author: weiqisen
 * @date: 2018/11/12 11:35
 * @description:
 */
public class UserRes extends SystemAccount implements Serializable {
    private static final long serialVersionUID = 6717800085953996702L;

    private Collection<Map> roles = Lists.newArrayList();
    /**
     * 用户权限
     */
    private Collection<BaseAuthority> authorities = Lists.newArrayList();
    /**
     * 第三方账号
     */
    private String thirdParty;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 城市
     */
    private String city;

    /**
     * 用户描述
     */
    private String userDesc;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 租户id
     */
    private Long tenantId;


    private Long userId;

    public Collection<BaseAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<BaseAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getThirdParty() {
        return thirdParty;
    }

    public void setThirdParty(String thirdParty) {
        this.thirdParty = thirdParty;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Collection<Map> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Map> roles) {
        this.roles = roles;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserDesc() {
        return userDesc;
    }

    public void setUserDesc(String userDesc) {
        this.userDesc = userDesc;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
