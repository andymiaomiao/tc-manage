package com.opencloud.device.configuration;

import com.opencloud.device.mqtt.impl.MqttMessageReceiver;
import com.opencloud.device.properties.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


/**
 * mqtt入站消息处理配置
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 14:50
 */
@Configuration
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttInConfig {

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttMessageReceiver mqttMessageReceiver;

    /**
     * mqtt消息入站通道，订阅消息后消息进入的通道。
     * 可创建多个入站通道，对应多个不同的消息生产者。
     *
     * @return
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 对于当前应用来讲，接收的mqtt消息的生产者。将生产者绑定到mqtt入站通道，即通过入站通道进入的消息为生产者生产的消息。
     * 可创建多个消息生产者，对应多个不同的消息入站通道，同时生产者监听不同的topic
     *
     * @return
     */
    @Bean
    public MessageProducer channelInbound(MessageChannel mqttInputChannel, MqttPahoClientFactory factory) {
        String clientId = "h-backend-mqtt-in-" + System.currentTimeMillis();
//        String[] topStr = mqttProperties.getToplist().toArray(new String[mqttProperties.getToplist().size()]);
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId, factory, mqttProperties.getTopArray());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel);
        return adapter;
    }

    /**
     * mqtt入站消息处理工具，对于指定消息入站通道接收到生产者生产的消息后处理消息的工具。
     *
     * @return
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler mqttMessageHandler() {
        return this.mqttMessageReceiver;
    }
}
