package com.opencloud.device.properties;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-18 09:43
 */
@Data
public class StorageQiniuProperties {
    private String prefix = "";
    private String ak = "";
    private String sk = "";
    private String bucket = "";
}
