package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemRole;
import com.weiqisen.tc.form.res.TenantMenuAllRes;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import com.weiqisen.tc.security.BaseAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author weiqisen
 */
@Repository
public interface SystemRoleMapper extends SuperMapper<SystemRole> {

    /**
     * 查询角色列表
     *
     * @param params
     * @return
     */
    List<SystemRole> selectRoleList(Map params);

    /**
     * 查询角色权限列表
     *
     * @param roleId
     * @param roleType
     * @return
     */
    List<BaseAuthority> findActionByRole(@Param("roleId") Long roleId, @Param("roleType") String roleType);

    List<TenantMenuAllRes> selectRoleMenuAllList(@Param("roleType") Integer roleType);

    List<TenantMenuAllRes> selectRoleMenuAuthorityList(@Param("roleId") Long roleId,  @Param("roleType") Integer roleType);

    List<TenantMenuAllRes> selectCustRoleMenuAllList(@Param("roleType") Integer roleType);

    List<TenantMenuAllRes> selectCustRoleMenuAuthorityList(@Param("roleId") Long roleId, @Param("roleType") Integer roleType);

    Integer selectSystemRoleByUserIdAndTenant(@Param("userId") List<Long> userId, @Param("userType") Integer userType);
}
