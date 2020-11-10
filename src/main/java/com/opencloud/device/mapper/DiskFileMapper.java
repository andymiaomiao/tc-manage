package com.opencloud.device.mapper;

import com.opencloud.device.entity.DiskFile;
import com.opencloud.device.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskFileMapper extends SuperMapper<DiskFile> {

    void deleteOtherThree(@Param("tenantId") Long tenantId);
}