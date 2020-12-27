package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemRole;
import com.weiqisen.tc.entity.SystemRoleUser;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author weiqisen
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
