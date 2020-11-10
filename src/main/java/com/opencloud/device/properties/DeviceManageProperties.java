package com.opencloud.device.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-01 14:45
 */
@Data
//@Component
@ConfigurationProperties(prefix = "oss.socket")
public class DeviceManageProperties {
    private OssTraceProperties trace;

    private OssSaveProperties save;
}
