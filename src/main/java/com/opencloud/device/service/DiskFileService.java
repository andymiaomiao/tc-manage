package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.DiskFile;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.ResultBody;
import com.opencloud.device.mybatis.base.service.IBaseService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-18 10:43
 */
public interface DiskFileService extends IBaseService<DiskFile> {

    IPage<DiskFile> findPage(PageParams pageParams);

    void removeBatch(List<Map<String, Object>> fileList);

    void deleteOtherThree();

    void remove(Long fileId);
}
