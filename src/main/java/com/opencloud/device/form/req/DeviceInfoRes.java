package com.opencloud.device.form.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 16:52
 */
@Data
public class DeviceInfoRes implements Serializable {
    private Long taskId;
    private Integer taskStatus;
    private String taskCode;
    private String deviceSn;
    private String softwareVersion;
    private Integer onlineStatus;
    private String deviceIp;
    private String uri;
    private String totalSize;
    private String completedSize;
    private AlarmInfoRes alarmInfo;

    private List<DeviceInfoRes> deviceInfoResList;

//    public DeviceInfoRes(Long taskId,Integer status,String code,String deviceSn,String softwareVersion,Integer onlineStatus,String deviceIp,AlarmInfoRes alarmInfoRes){
//        this.taskId = taskId;
//        this.taskStatus = status;
//        this.taskCode = code;
//        this.deviceSn = deviceSn;
//        this.softwareVersion = softwareVersion;
//        this.onlineStatus = onlineStatus;
//        this.deviceIp = deviceIp;
//        this.alarmInfo = alarmInfoRes;
//    }
}
