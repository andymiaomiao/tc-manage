package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemMenu;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author weiqisen
 */
@Repository
public interface SystemMenuMapper extends SuperMapper<SystemMenu> {
    List<SystemMenu> findMenuByUser(@Param("map") Map<String, Object> paramMap);

    List<SystemMenu> findCustMenuByUser(@Param("map") Map<String, Object> paramMap);
}
