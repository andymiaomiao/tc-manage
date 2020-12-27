package com.weiqisen.tc.properties;

import lombok.Data;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-07-18 09:41
 */
@Data
public class StorageDiskProperties {

    private String location = "uploadfiles";

    private String prefix = "";

    private String downloadto ="";
}
