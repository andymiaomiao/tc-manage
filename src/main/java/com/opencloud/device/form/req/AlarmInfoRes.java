package com.opencloud.device.form.req;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 16:55
 */
@Data
public class AlarmInfoRes implements Serializable {
    private Long deviceId;
    private Long taskId;
    private Integer taskStatus;
    private String taskCode;
    private Integer ethernet;
    private Integer modem;
    private Integer ram;
    private Integer data;
    private Integer simCard;
}
