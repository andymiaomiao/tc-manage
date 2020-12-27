package com.weiqisen.tc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-07-06 15:22
 */
@Configuration
//@EnableConfigurationProperties(DeviceManageProperties.class)
public class DeviceManageConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTokenStore redisTokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

//    /**
//     * 令牌存放
//     *
//     * @return
//     */
//    @Bean
//    public TokenStore tokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }
}
