package com.opencloud.device.form.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 16:52
 */
@Data
public class DeviceLofInfoRes implements Serializable {
    private Long taskId;
    private Integer taskStatus;
    private String taskCode;
    private String deviceSn;
    private String url;
    private String totalSize;
    private String completedSize;
    private String fileName;
    private String realName;
}
