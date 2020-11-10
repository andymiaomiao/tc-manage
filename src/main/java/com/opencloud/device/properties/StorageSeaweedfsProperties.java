package com.opencloud.device.properties;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-18 09:45
 */
@Data
public class StorageSeaweedfsProperties {
    private String host = "";
    private int port = 9333;
    private int timeout = 5000;
}
