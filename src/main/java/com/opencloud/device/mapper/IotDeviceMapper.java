package com.opencloud.device.mapper;


import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface IotDeviceMapper extends SuperMapper<IotDevice> {
    Map<String, Object> selectDeviceOnline();

    Map<String,Object> selectDeviceAlarm();

    List<Map<String, Object>> selectDeviceDistList();

    List<Map<String, Object>> selectDeviceCateOnline();

    List<Map<String, Object>> selectDeviceCateOffline();
}
