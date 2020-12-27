package com.weiqisen.tc.mapper;

import com.weiqisen.tc.entity.DiskFile;
import com.weiqisen.tc.mybatis.base.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskFileMapper extends SuperMapper<DiskFile> {

    void deleteOtherThree(@Param("tenantId") Long tenantId);
}
