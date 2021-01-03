package com.weiqisen.tc.mapper;


import com.weiqisen.tc.entity.SystemApi;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface SystemApiMapper extends SuperMapper<SystemApi> {
    List<SystemApi> findActionList(Map map);

    List<SystemApi> findClientList(Map map);

    List<SystemApi> findApiList(Map map);

    void deleteApiAction(Map map);

    void batchUpdateServiceApi(@Param("list") List<SystemApi> systemApiList);

    List<SystemApi> findMenuAllList(Map map);

    List<SystemApi> findMenuGrantList(Map map);
}
