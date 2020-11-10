package com.opencloud.device.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.SystemUser;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author liuyadu
 */
@Repository
public interface SystemUserMapper extends SuperMapper<SystemUser> {
    IPage<Map<String, Object>> selectUserPage(PageParams pageParams, @Param("map") Map<String, Object> requestMap);

    IPage<Map<String, Object>> selectAdminUserPage(PageParams pageParams, @Param("map") Map<String, Object> requestMap);
}
