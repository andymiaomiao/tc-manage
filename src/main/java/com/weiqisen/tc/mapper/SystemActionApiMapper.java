package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemActionApi;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import com.weiqisen.tc.security.BaseAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author weiqisen
 */
@Repository
public interface SystemActionApiMapper extends SuperMapper<SystemActionApi> {

    /**
     * 查询操作权限列表
     *
     * @param userId
     * @param roleType
     * @return
     */
    List<BaseAuthority> selectAuthorityByUser(@Param("userId") Long userId, @Param("roleType") String roleType);
}
