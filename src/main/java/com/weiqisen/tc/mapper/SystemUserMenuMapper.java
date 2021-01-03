package com.weiqisen.tc.mapper;


import com.weiqisen.tc.entity.SystemUserMenu;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author liuyadu
 */
@Repository
public interface SystemUserMenuMapper extends SuperMapper<SystemUserMenu> {
    void batchGrantUserMenu(@Param("userId") Long userId, @Param("menuIds") List<String> menuIds);
}
