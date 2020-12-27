package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemTenant;
import com.weiqisen.tc.form.res.TenantMenuAllRes;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author weiqisen
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
