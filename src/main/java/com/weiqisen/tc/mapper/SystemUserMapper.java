package com.weiqisen.tc.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemUser;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author weiqisen
 */
@Repository
public interface SystemUserMapper extends SuperMapper<SystemUser> {
    IPage<Map<String, Object>> selectUserPage(PageParams pageParams, @Param("map") Map<String, Object> requestMap);

    IPage<Map<String, Object>> selectAdminUserPage(PageParams pageParams, @Param("map") Map<String, Object> requestMap);
}
