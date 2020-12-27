package com.weiqisen.tc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author weiqisen
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
