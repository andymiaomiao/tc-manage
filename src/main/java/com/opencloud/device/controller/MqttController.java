package com.opencloud.device.controller;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.mqtt.MqttMessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 14:57
 */
@Slf4j
@RestController
@RequestMapping("/public/mqtt")
@ConditionalOnProperty(value = "mqtt.enabled", havingValue = "true")
public class MqttController {

    @Autowired
    private MqttMessageSender mqttMessageSender;

    /**
     * 发送mqtt消息
     *
     * @param topic 消息主题
     * @param data  消息内容
     * @return
     */
    @PostMapping("/send")
    public ResultBody send(String topic, String data) {
        log.info("开始发送mqtt消息,主题：{},消息：{}", topic, data);
        if (StringUtils.isNotBlank(topic)) {
            this.mqttMessageSender.sendToMqtt(topic, data);
        } else {
            this.mqttMessageSender.sendToMqtt(data);
        }
        log.info("发送mqtt消息完成,主题：{},消息：{}", topic, data);
        return ResultBody.ok();
    }



}
