package com.opencloud.device.configuration;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.opencloud.device.exception.BaseRestResponseErrorHandler;
import com.opencloud.device.exception.GlobalExceptionHandler;
import com.opencloud.device.mybatis.ModelMetaObjectHandler;
import com.opencloud.device.security.ServerConfiguration;
import com.opencloud.device.security.SocialProperties;
import com.opencloud.device.utils.RestUtil;
import com.opencloud.device.utils.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 默认配置类
 *
 * @author liuyadu
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({CommonProperties.class,  SocialProperties.class})
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ServerConfiguration.class)
    public ServerConfiguration serverConfiguration(){
        return new ServerConfiguration();
    }

//    /**
//     * xss过滤
//     * body缓存
//     *
//     * @return
//     */
//    @Bean
//    public FilterRegistrationBean baseFilter() {
//        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new BaseFilter());
//        log.info("BaseFilter [{}]", filterRegistrationBean);
//        return filterRegistrationBean;
//    }


    /**
     * 默认加密配置
     *
     * @return
     */
    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new StandardPasswordEncoder();
        log.info("默认密码加密类 [{}]", encoder);
        return encoder;
    }


    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("Spring上下文静态工具类 [{}]", holder);
        return holder;
    }

    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        log.info("全局异常处理器 [{}]", exceptionHandler);
        return exceptionHandler;
    }

//    /**
//     * 资源扫描类
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean(RequestMappingScan.class)
//    public RequestMappingScan resourceAnnotationScan(RedisTemplate redisTemplate) {
//        RequestMappingScan scan = new RequestMappingScan(redisTemplate);
//        log.info("资源扫描类.[{}]", scan);
//        return scan;
//    }

    /**
     * 自定义Oauth2请求类
     *
     * @param commonProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RestUtil.class)
    public RestUtil baseRestTemplate(RestTemplate restTemplate, CommonProperties commonProperties,
//                                     BusProperties busProperties,
                                     ApplicationEventPublisher publisher) {
        restTemplate.setErrorHandler(new BaseRestResponseErrorHandler());
        RestUtil restUtil = new RestUtil(restTemplate,commonProperties,
//                busProperties,
                publisher);
        //设置自定义ErrorHandler
        log.info("RestUtil [{}]", restUtil);
        return restUtil;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        //设置自定义ErrorHandler
        restTemplate.setErrorHandler(new BaseRestResponseErrorHandler());
        log.info("RestTemplate [{}]", restTemplate);
        return restTemplate;
    }

//    @Bean
//    @ConditionalOnMissingBean(DbHealthIndicator.class)
//    public DbHealthIndicator dbHealthIndicator() {
//        DbHealthIndicator dbHealthIndicator = new DbHealthIndicator();
//        log.info("DbHealthIndicator [{}]", dbHealthIndicator);
//        return dbHealthIndicator;
//    }


    /**
     * 分页插件
     */
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        log.info("分页插件 [{}]", paginationInterceptor);
        return paginationInterceptor;
    }

    /**
     * 自动填充模型数据
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ModelMetaObjectHandler.class)
    public ModelMetaObjectHandler modelMetaObjectHandler() {
        ModelMetaObjectHandler metaObjectHandler = new ModelMetaObjectHandler();
        log.info("自动填充模型数据处理器 [{}]", metaObjectHandler);
        return metaObjectHandler;
    }

}
