package com.opencloud.device.properties;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-18 09:45
 */
@Data
public class StorageAliProperties {

    private String endpoint = "";
    private String accesskeyid = "";
    private String accesskeysecret = "";
    private String bucketname = "";
    private String downloadkey = "";
}
