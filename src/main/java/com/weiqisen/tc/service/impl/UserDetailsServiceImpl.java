package com.weiqisen.tc.service.impl;

import com.weiqisen.tc.form.res.UserRes;
import com.weiqisen.tc.model.SystemConstants;
import com.weiqisen.tc.security.BaseUserDetails;
import com.weiqisen.tc.security.SocialProperties;
import com.weiqisen.tc.service.SystemUserService;
import com.weiqisen.tc.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Security用户信息获取实现类
 *
 * @author weiqisen
 */
@Slf4j
@Service("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SocialProperties socialProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request =  WebUtil.getHttpServletRequest();
        Map<String, String> parameterMap = WebUtil.getParamMap(request);
        // 第三方登录标识
        String thirdParty = parameterMap.get("thirdParty");
        String loginType = parameterMap.get("loginType");
        UserRes account = systemUserService.login(username,thirdParty,loginType);
        if (account == null || account.getAccountId() == null) {
            throw new UsernameNotFoundException("系统用户 " + username + " 不存在!");
        }
        Long accountId = account.getAccountId();
        boolean accountNonLocked = account.getStatus().intValue() != SystemConstants.ACCOUNT_STATUS_LOCKED;
        boolean credentialsNonExpired = true;
        boolean enabled = account.getStatus().intValue() == SystemConstants.ACCOUNT_STATUS_NORMAL ? true : false;
        boolean accountNonExpired = true;
        BaseUserDetails userDetails = new BaseUserDetails();
        userDetails.setAccountId(accountId);
        userDetails.setAccountNonLocked(accountNonLocked);
        userDetails.setAccountNonExpired(accountNonExpired);
        userDetails.setUsername(username);
        userDetails.setAccountId(accountId);
        userDetails.setDomain(account.getDomain());
        userDetails.setUserId(account.getUserId());
        userDetails.setMobile(account.getMobile());
        userDetails.setEmail(account.getEmail());
        userDetails.setCity(account.getCity());
        userDetails.setUserDesc(account.getUserDesc());
        userDetails.setPassword(account.getPassword());
        userDetails.setNickName(account.getNickName());
        userDetails.setAuthorities(account.getAuthorities());
        userDetails.setAvatar(account.getAvatar());
        userDetails.setAccountType(account.getAccountType());
        userDetails.setCredentialsNonExpired(credentialsNonExpired);
        userDetails.setEnabled(enabled);
        userDetails.setClientId(socialProperties.getClient().get("device").getClientId());
        return userDetails;
    }
}
