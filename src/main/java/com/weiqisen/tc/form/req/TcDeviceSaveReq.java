package com.weiqisen.tc.form.req;


import lombok.Data;

/**
 * 系统-设备信息
 *
 * @author weiqisen
 */
@Data

public class TcDeviceSaveReq {

    private Long deviceId;

    private Long categoryId;

    private String deviceName;

    private String deviceSn;

    private String deviceRom;

    private String model;

    private String appearance;

    private String detail;

    private int status;

    private int cost;

    private String lists;

    private String battery;

    private String systemVersion;

}
