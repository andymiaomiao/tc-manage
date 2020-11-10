package com.opencloud.device.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 15:56
 */
@Data
@Component
@ConfigurationProperties(prefix = "mqtt")
public class MqttProperties {
    private Boolean enabled;
    private String host;
    private String password;
    private String username;
    private String[] topArray;
//    private List<String> toplist;
}

