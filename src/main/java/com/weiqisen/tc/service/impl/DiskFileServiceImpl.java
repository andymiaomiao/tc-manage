package com.weiqisen.tc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.weiqisen.tc.entity.DiskFile;
import com.weiqisen.tc.mapper.DiskFileMapper;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.properties.StorageProperties;
import com.weiqisen.tc.service.DiskFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-07-18 11:22
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DiskFileServiceImpl extends BaseServiceImpl<DiskFileMapper, DiskFile> implements DiskFileService {

    @Autowired
    private StorageProperties properties;

    @Override
    public IPage<DiskFile> findPage(PageParams pageParams) {
        DiskFile query = pageParams.mapToBean(DiskFile.class);
        QueryWrapper<DiskFile> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getFileName()), DiskFile::getFileName, query.getFileName())
                .eq(ObjectUtils.isNotEmpty(query.getFileFlag()), DiskFile::getFileFlag, query.getFileFlag())
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), DiskFile::getTenantId, query.getTenantId())
                .orderByDesc(DiskFile::getUpdateTime);
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    @Override
    public void removeBatch(List<Map<String, Object>> fileList) {
        if(fileList!=null&&fileList.size()>0){
            fileList.stream().forEach(item->{
                String fileName = properties.getDisk().getLocation()+ File.separator+ item.get("fileName");
                String realName = properties.getDisk().getLocation()+ File.separator+ item.get("realName");
                removeFile(fileName);
                removeFile(realName);
            });
        }
    }

    @Override
    public void deleteOtherThree() {
        LambdaQueryWrapper<DiskFile> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(DiskFile::getFileFlag,"1")
                .orderByDesc(DiskFile::getCreateTime)
                .last("limit 3,100");
        List<DiskFile> diskFiles = baseMapper.selectList(queryWrapper);
        if(diskFiles!=null&&diskFiles.size()>0){
            List<Long> collect = diskFiles.stream().map(m -> m.getFileId()).collect(Collectors.toList());
            baseMapper.deleteBatchIds(collect);
        }
    }

    @Override
    public void remove(Long fileId) {
        DiskFile diskFile = baseMapper.selectById(fileId);
        if(diskFile!=null){
            String fileName = properties.getDisk().getLocation()+ File.separator+ diskFile.getFileName();
            String realName = properties.getDisk().getLocation()+ File.separator+ diskFile.getNowName();
            removeFile(fileName);
            removeFile(realName);
            removeById(fileId);
        }
    }

    public void removeFile(String fileName){
        File realFile = new File(fileName);
        if(realFile.isFile() && realFile.exists()){
            realFile.delete();
        }
    }
}
