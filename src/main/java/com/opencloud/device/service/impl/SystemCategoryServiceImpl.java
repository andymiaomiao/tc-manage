package com.opencloud.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.opencloud.device.entity.SystemCategory;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.mapper.SystemCategoryMapper;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.SystemCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 通用账号
 *
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemCategoryServiceImpl extends BaseServiceImpl<SystemCategoryMapper, SystemCategory> implements SystemCategoryService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemCategory> findPage(PageParams pageParams) {
        SystemCategory query = pageParams.mapToBean(SystemCategory.class);
        QueryWrapper<SystemCategory> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .like(ObjectUtils.isNotEmpty(query.getName()), SystemCategory::getName, query.getName())
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), SystemCategory::getTenantId, query.getTenantId())
                .eq(ObjectUtils.isNotEmpty(query.getCategoryType()), SystemCategory::getCategoryType, query.getCategoryType())
                .eq(SystemCategory::getStatus,1)
                .orderByDesc(SystemCategory::getCreateTime,SystemCategory::getSort);
        return baseMapper.selectPage(new Page(pageParams.getPage(), pageParams.getLimit()), queryWrapper);
    }

    /**
     * 分页查询
     *
     * @param requestParams
     * @return
     */
    @Override
    public List<SystemCategory> findList(RequestParams requestParams) {
        SystemCategory query = requestParams.mapToBean(SystemCategory.class);
        QueryWrapper<SystemCategory> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(ObjectUtils.isNotEmpty(query.getTenantId()), SystemCategory::getTenantId, query.getTenantId())
                .eq(SystemCategory::getStatus,1)
                .orderByDesc(SystemCategory::getSort);
        return baseMapper.selectList(queryWrapper);
    }


    /**
     * 检查分类名称是否存在
     *
     * @param name
     * @return
     */
    @Override
    public Boolean isExist(String name,Long tenantId) {
        QueryWrapper<SystemCategory> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemCategory::getName, name)
                .eq(SystemCategory::getTenantId,tenantId);
        int count = count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加分类
     *
     * @param category
     * @return
     */
    @Override
    public void add(SystemCategory category) {
        if (isExist(category.getName(),category.getTenantId())) {
            throw new BaseException(String.format("%s分类已存在!", category.getName()));
        }
        save(category);
    }

    /**
     * 修改分类
     *
     * @param category
     * @return
     */
    @Override
    public void update(SystemCategory category) {
        SystemCategory saved = getById(category.getCategoryId());
        if (saved == null) {
            throw new BaseException("信息不存在!");
        }
        if (!saved.getName().equals(category.getName())) {
            // 和原来不一致重新检查唯一性
            if (isExist(category.getName(),category.getTenantId())) {
                throw new BaseException(String.format("%s分类已存在!", category.getName()));
            }
        }
        category.setUpdateTime(new Date());
        updateById(category);
    }

   /**
     * 移除接口
     *
     * @param categoryId
     * @return
     */
    @Override
    public void remove(Long categoryId) {
        SystemCategory category = getById(categoryId);
        removeById(category);
    }
}
