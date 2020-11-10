package com.opencloud.device.model;

/**
 * 通用权限常量
 *
 * @author liuyadu
 */
public class DeviceConstants {

    /**
     * 服务名称
     */
    public static final String SYSTEM_SERVER = "device-service";

    /**
     * 服务名称
     */
    public static final String OSS_SERVER = "oss-service";

    /**
     * 设备信息查询
     * v1/camera/info/cloud2device/{deviceid}
     */
    public static final String CLOUD_DEVICE_INFO = "v1/camera/info/cloud2device/";

    /**
     * 推送版本文件信息
     * v1/camera/soft/cloud2device/{deviceid}
     */
    public static final String CLOUD_DEVICE_SOFT = "v1/camera/soft/cloud2device/";

    /**
     * 日志文件采集通知
     * v1/camera/log/cloud2device/{deviceid}
     */
    public static final String CLOUD_DEVICE_LOG = "v1/camera/log/cloud2device/";

    /**
     * 告警问题下发
     * v1/camera/alarm/cloud2device/{deviceid}
     */
    public static final String CLOUD_DEVICE_ALARM = "v1/camera/alarm/cloud2device/";

    public static final String DEVICE_CHANNL_1001 = "1001";

    public static final String DEVICE_CHANNL_2001 = "2001";

    public static final String DEVICE_CHANNL_3001 = "3001";

    public static final String DEVICE_CHANNL_4001 = "4001";

    public static final String DEVICE_TYPE_INFO = "info";

    public static final String DEVICE_TYPE_SOFT = "soft";

    public static final String DEVICE_TYPE_LOG = "log";

    public static final String DEVICE_TYPE_ALARM = "alarm";



}
