package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemTenantAction;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author weiqisen
 */
@Repository
public interface SystemTenantActionMapper extends SuperMapper<SystemTenantAction> {
    void batchGrantTenantAction(@Param("tenantId") Long tenantId, @Param("menuIds") List<String> menuIds);
}
