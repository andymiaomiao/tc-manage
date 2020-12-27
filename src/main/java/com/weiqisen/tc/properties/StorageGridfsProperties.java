package com.weiqisen.tc.properties;

import lombok.Data;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-07-18 09:44
 */
@Data
public class StorageGridfsProperties {
    private String host = "";
    private int port = 27017;
    private String dbname = "";
    private String collectionname = "";
}
