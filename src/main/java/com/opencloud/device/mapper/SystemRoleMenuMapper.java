package com.opencloud.device.mapper;

import com.opencloud.device.entity.SystemRoleMenu;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemRoleMenuMapper extends SuperMapper<SystemRoleMenu> {
    void batchGrantRoleMenu(@Param("roleId") Long roleId, @Param("menuIds") List<String> menuIds);
}
