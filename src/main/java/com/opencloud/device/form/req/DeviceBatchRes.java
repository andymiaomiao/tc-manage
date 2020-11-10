package com.opencloud.device.form.req;

import com.opencloud.device.entity.IotDevice;
import lombok.Data;

import java.util.List;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-28 10:41
 */
@Data
public class DeviceBatchRes {
    List<IotDevice> deviceList;
}
