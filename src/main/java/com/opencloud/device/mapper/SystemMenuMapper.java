package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemMenu;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface SystemMenuMapper extends SuperMapper<SystemMenu> {
    List<SystemMenu> findMenuByUser(@Param("map") Map<String, Object> paramMap);

    List<SystemMenu> findCustMenuByUser(@Param("map") Map<String, Object> paramMap);
}
