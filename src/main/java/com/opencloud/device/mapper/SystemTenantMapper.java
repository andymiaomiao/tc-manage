package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemTenant;
import com.opencloud.device.form.res.TenantMenuAllRes;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemTenantMapper extends SuperMapper<SystemTenant> {

    List<TenantMenuAllRes> selectTenantMenuAllList(@Param("tenantId") Long tenantId, @Param("tenantType") Integer tenantType);

    List<TenantMenuAllRes> selectTenantMenuGrantList(@Param("tenantId") Long tenantId, @Param("tenantType") Integer tenantType);

//    /**
//     * 查询角色列表
//     * @param params
//     * @return
//     */
//    List<SystemTenant> selectRoleList(Map params);
}
