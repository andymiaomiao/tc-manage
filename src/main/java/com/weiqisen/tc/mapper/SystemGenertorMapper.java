package com.weiqisen.tc.mapper;


import com.weiqisen.tc.entity.SystemGenertor;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import com.weiqisen.tc.security.BaseAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemGenertorMapper extends SuperMapper<SystemGenertor> {

    /**
     * 查询操作权限列表
     *
     * @param userId
     * @param roleType
     * @return
     */
    List<BaseAuthority> selectAuthorityByUser(@Param("userId") Long userId, @Param("roleType") String roleType);

    int deleteActionApi(Long actionId);
}
