package com.weiqisen.tc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author weiqisen
 * @date 28/12/2020 10:37 上午
 */
@Slf4j
@Configuration
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {

    @Autowired
    private TokenStore tokenStore;

    private BearerTokenExtractor tokenExtractor = new BearerTokenExtractor();
    public LogoutSuccessHandler() {
        // 重定向到原地址
        this.setUseReferer(true);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            // 解密请求头
            authentication =  tokenExtractor.extract(request);
            if(authentication!=null && authentication.getPrincipal()!=null){
                String tokenValue = authentication.getPrincipal().toString();
                log.debug("revokeToken tokenValue:{}",tokenValue);
                // 移除token
                tokenStore.removeAccessToken(tokenStore.readAccessToken(tokenValue));
            }
        }catch (Exception e){
            log.error("revokeToken error:{}",e);
        }
        super.onLogoutSuccess(request, response, authentication);
    }



}
