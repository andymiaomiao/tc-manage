package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemRole;
import com.opencloud.device.entity.SystemRoleUser;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemRoleUserMapper extends SuperMapper<SystemRoleUser> {
    /**
     * 查询系统用户角色
     *
     * @param userId
     * @return
     */
    List<SystemRole> selectUsersByRoleId(@Param("userId") Long userId);

    /**
     * 查询系统用户角色
     *
     * @param userId
     * @return
     */
    Long selectRoleByUserId(@Param("userId") Long userId);

    /**
     * 查询用户角色ID列表
     *
     * @param userId
     * @return
     */
    List<Long> selectUsersIdByRoleId(@Param("userId") Long userId);

}
