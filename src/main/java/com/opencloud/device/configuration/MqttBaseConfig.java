package com.opencloud.device.configuration;



import com.opencloud.device.properties.MqttProperties;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

import java.util.List;

/**
 * mqtt消息处理配置
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 14:49
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttBaseConfig {

    @Autowired
    private MqttProperties mqttProperties;

    /**
     * 构造一个默认的mqtt客户端操作bean
     *
     * @return
     */
    @Bean
    public MqttPahoClientFactory factory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        //如果配置了用户密码这里需要对应设置
//        options.setUserName(mqttProperties.getUsername());
//        options.setPassword(mqttProperties.getPwd().toCharArray());
        options.setServerURIs(new String[]{mqttProperties.getHost()});
        factory.setConnectionOptions(options);
        return factory;
    }

}
