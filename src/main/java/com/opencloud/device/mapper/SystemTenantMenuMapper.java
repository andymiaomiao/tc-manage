package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemTenantMenu;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemTenantMenuMapper extends SuperMapper<SystemTenantMenu> {
    void batchGrantTenantMenu(@Param("tenantId") Long tenantId, @Param("menuIds") List<String> menuIds);
}
