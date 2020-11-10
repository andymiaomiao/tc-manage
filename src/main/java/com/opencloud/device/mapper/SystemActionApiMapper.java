package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemActionApi;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import com.opencloud.device.security.BaseAuthority;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
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
