package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.SystemRoleMenu;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author weiqisen
 */
@Repository
public interface SystemRoleMenuMapper extends SuperMapper<SystemRoleMenu> {
    void batchGrantRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<String> menuIds);
}
