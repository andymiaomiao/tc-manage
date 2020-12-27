package com.weiqisen.tc.configuration;

import com.weiqisen.tc.properties.DeviceManageProperties;
import com.weiqisen.tc.properties.StorageProperties;
import com.weiqisen.tc.security.ServerConfiguration;
import com.weiqisen.tc.security.SocialProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LYD
 */
@Configuration
@EnableConfigurationProperties({StorageProperties.class, DeviceManageProperties.class, SocialProperties.class})
public class OssConfiguration {

    @Bean
    @ConditionalOnMissingBean(ServerConfiguration.class)
    public ServerConfiguration serverConfiguration(){
        return new ServerConfiguration();
    }

//    @Autowired
//    private StorageProperties prop;
//
//    @Autowired
//    private QiniuService qiniuService;
//
//    @Autowired
//    private AliService aliService;
//
//    @Autowired
//    private FastdfsServcice fastdfsServcice;
//
//    @Autowired
//    private MongoService mongoService;
//
//    @Autowired
//    private SeaweedfsService seaweedfsService;
//
//    @Autowired
//    private RedisConnectionFactory redisConnectionFactory;
//
//
////    /**
////     * 访问日志队列
////     *
////     * @return
////     */
////    @Bean
////    public Queue accessLogsQueue() {
////        Queue queue = new Queue(CommonConstants.API_ACCESS_LOGS);
////        return queue;
////    }
////
////    /**
////     * 登陆日志
////     * @return
////     */
////    @Bean
////    public Queue accountLogsQueue() {
////        Queue queue = new Queue(CommonConstants.ACCOUNT_LOGS);
////        return queue;
////    }
//
//    /**
//     * oauth2 令牌redis存储
//     *
//     * @return
//     */
//    @Bean
//    public RedisTokenStore redisTokenStore() {
//        return new RedisTokenStore(redisConnectionFactory);
//    }
//
////    /**
////     * oauth2客户端数据库存储
////     *
////     * @return
////     */
////    @Bean
////    public JdbcClientDetailsService clientDetailsService() {
////        JdbcClientDetailsService jdbcClientDetailsService = new JdbcClientDetailsService(dataSource);
////        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
////        return jdbcClientDetailsService;
////    }
////
////    public static void main(String args[]){
////        PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
////        String encode = passwordEncoder.encode("123456");
////        System.out.println(encode);
////    }
//
//
//
//    @Bean
//    CommandLineRunner init(StorageService storageService) {
//        return (args) -> {
//            //storageService.deleteAll();
//            storageService.init();
//
//            initCache();
//
//            registerStoreSource();
//        };
//    }
//
////	@Bean
////    public SpringDataDialect springDataDialect() {
////        return new SpringDataDialect();
////    }
//
//    public void registerStoreSource() {
//        if (prop.isToqiniu()){
//            StoreSource.RegisterListensers(qiniuService);
//        }
//
//        if (prop.isToalioss()){
//            StoreSource.RegisterListensers(aliService);
//        }
//
//        if (prop.isTofastdfs()){
//            StoreSource.RegisterListensers(fastdfsServcice);
//        }
//
//        if (prop.isTomongodb()){
//            StoreSource.RegisterListensers(mongoService);
//        }
//
//        if (prop.isToseaweedfs()){
//            StoreSource.RegisterListensers(seaweedfsService);
//        }
//    }
//
//    public void initCache() {
//        //TODO  cache test
//        UsesCache.files = 1000000;
//        UsesCache.groups = 1000;
//        UsesCache.usedspace = 1000000000;
//    }
}
