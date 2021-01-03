package com.weiqisen.tc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.weiqisen.tc.entity.SystemGenertor;
import com.weiqisen.tc.exception.BaseException;
import com.weiqisen.tc.mapper.SystemGenertorMapper;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.service.SystemGenertorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemGenertorServiceImpl extends BaseServiceImpl<SystemGenertorMapper, SystemGenertor> implements SystemGenertorService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemGenertor> findPage(PageParams pageParams) {
        SystemGenertor query = pageParams.mapToBean(SystemGenertor.class);
        QueryWrapper<SystemGenertor> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getUrl()), SystemGenertor::getUrl, query.getUrl())
                .likeRight(ObjectUtils.isNotEmpty(query.getUsername()), SystemGenertor::getUsername, query.getUsername());
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }

    /**
     * 检查Action编码是否存在
     *
     * @param url
     * @return
     */
    @Override
    public Boolean isExist(String url,String port,String schemas) {
        QueryWrapper<SystemGenertor> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(SystemGenertor::getUrl,url)
                .eq(SystemGenertor::getPort,port)
                .eq(SystemGenertor::getSchemas,schemas);
        int count = count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加Action操作
     *
     * @param aciton
     * @return
     */
    @Override
    public SystemGenertor add(SystemGenertor aciton) {
        if (isExist(aciton.getUrl(),aciton.getPort(),aciton.getSchemas())) {
            throw new BaseException(String.format("%surl地址已存在!", aciton.getUrl()));
        }
        aciton.setCreateTime(new Date());
        aciton.setUpdateTime(aciton.getCreateTime());
        save(aciton);
        // 同步权限表里的信息
//        systemAuthorityService.saveOrUpdateAuthority(aciton.getActionId(), ResourceType.action);
        return aciton;
    }

    /**
     * 修改Action操作
     *
     * @param aciton
     * @return
     */
    @Override
    public SystemGenertor update(SystemGenertor aciton) {
        SystemGenertor saved = getById(aciton.getGenertorId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在", aciton.getGenertorId()));
        }
        aciton.setUpdateTime(new Date());
        updateById(aciton);
        return aciton;
    }

    /**
     * 移除Action
     *
     * @param actionId
     * @return
     */
    @Override
    public void remove(Long actionId) {
        SystemGenertor aciton = getById(actionId);
        removeById(actionId);
    }
}
