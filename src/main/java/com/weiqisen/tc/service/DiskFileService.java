package com.weiqisen.tc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.DiskFile;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-07-18 10:43
 */
public interface DiskFileService extends IBaseService<DiskFile> {

    IPage<DiskFile> findPage(PageParams pageParams);

    void removeBatch(List<Map<String, Object>> fileList);

    void deleteOtherThree();

    void remove(Long fileId);
}
