package com.weiqisen.tc.mapper;


import com.weiqisen.tc.entity.SystemClient;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.springframework.stereotype.Repository;

/**
 * @author liuyadu
 */
@Repository
public interface SystemClientMapper extends SuperMapper<SystemClient> {

//    /**
//     * 查询操作权限列表
//     *
//     * @param userId
//     * @param roleType
//     * @return
//     */
//    List<BaseAuthority> selectAuthorityByUser(@Param("userId") Long userId, @Param("roleType") String roleType);
//
//    List<BaseAuthority> selectAppAuthorityByUser(@Param("userId") Long userId, @Param("tenantType") String tenantType, @Param("userType") String userType);
//
    int deleteClientApi(Long clientId);
}
