package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemTenantAction;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemTenantActionMapper extends SuperMapper<SystemTenantAction> {
    void batchGrantTenantAction(@Param("tenantId") Long tenantId, @Param("menuIds") List<String> menuIds);
}
