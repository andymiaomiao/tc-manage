package com.opencloud.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.IotDeviceLog;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface IotDeviceLogMapper extends SuperMapper<IotDeviceLog> {

    IPage<Map<String, Object>> selectLogPage(PageParams pageParams, @Param("map") Map<String, Object> requestMap);
}
