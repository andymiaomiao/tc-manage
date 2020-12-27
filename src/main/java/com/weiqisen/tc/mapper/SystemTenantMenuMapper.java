package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemTenantMenu;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author weiqisen
 */
@Repository
public interface SystemTenantMenuMapper extends SuperMapper<SystemTenantMenu> {
    void batchGrantTenantMenu(@Param("tenantId") Long tenantId, @Param("menuIds") List<String> menuIds);
}
