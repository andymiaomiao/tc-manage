package com.weiqisen.tc.mapper;


import com.weiqisen.tc.entity.SystemUserAction;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemUserActionMapper extends SuperMapper<SystemUserAction> {
    void batchGrantUserAction(@Param("userId") Long userId, @Param("menuIds") List<String> menuIds);
}
