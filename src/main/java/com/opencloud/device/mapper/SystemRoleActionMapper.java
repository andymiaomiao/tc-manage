package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemRoleAction;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemRoleActionMapper extends SuperMapper<SystemRoleAction> {
    void batchGrantRoleAction(@Param("roleId") Long roleId, @Param("menuIds") List<String> menuIds);
}
