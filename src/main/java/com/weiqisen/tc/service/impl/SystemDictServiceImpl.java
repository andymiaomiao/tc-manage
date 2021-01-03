package com.weiqisen.tc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;

import com.weiqisen.tc.entity.SystemDict;
import com.weiqisen.tc.exception.BaseException;
import com.weiqisen.tc.mapper.SystemDictMapper;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.service.SystemDictService;
import com.weiqisen.tc.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author liuyadu
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemDictServiceImpl extends BaseServiceImpl<SystemDictMapper, SystemDict> implements SystemDictService {

    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<SystemDict> findPage(PageParams pageParams) {
        SystemDict query = pageParams.mapToBean(SystemDict.class);
        QueryWrapper<SystemDict> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getDictType()), SystemDict::getDictType, query.getDictType())
                .likeRight(ObjectUtils.isNotEmpty(query.getDictName()), SystemDict::getDictName, query.getDictName())
                .likeRight(ObjectUtils.isNotEmpty(query.getDictCode()), SystemDict::getDictCode, query.getDictCode())
                .eq(ObjectUtils.isNotEmpty(query.getStatus()), SystemDict::getStatus, query.getStatus())
                .orderByDesc(SystemDict::getCreateTime);
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 获取服务列表
     *
     * @return
     */
    @Override
    public List<SystemDict> getServiceList(SystemDict query) {
        QueryWrapper<SystemDict> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .likeRight(ObjectUtils.isNotEmpty(query.getDictType()), SystemDict::getDictType, query.getDictType())
                .likeRight(ObjectUtils.isNotEmpty(query.getDictName()), SystemDict::getDictName, query.getDictName())
                .likeRight(ObjectUtils.isNotEmpty(query.getDictCode()), SystemDict::getDictCode, query.getDictCode())
                .eq(ObjectUtils.isNotEmpty(query.getStatus()), SystemDict::getStatus, query.getStatus())
                .orderByDesc(SystemDict::getCreateTime);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 添加角色
     *
     * @param server 角色
     * @return
     */
    @Override
    public SystemDict add(SystemDict server) {
        if (isExist(server.getDictName())) {
            throw new BaseException(String.format("%s字典名称已存在!", server.getDictName()));
        }
//        if (server.getStatus() == null) {
//            server.setStatus(SystemConstants.ENABLED);
//        }
//        if (server.getIsPersist() == null) {
//            server.setIsPersist(SystemConstants.DISABLED);
//        }
        server.setCreateTime(new Date());
        server.setUpdateTime(server.getCreateTime());
        save(server);
        return server;
    }

    /**
     * 更新角色
     *
     * @param server 角色
     * @return
     */
    @Override
    public SystemDict update(SystemDict server) {
        SystemDict saved = getById(server.getDictId());
        if (server == null) {
            throw new BaseException("信息不存在!");
        }
        if (!saved.getDictName().equals(server.getDictName())) {
            // 和原来不一致重新检查唯一性
            if (isExist(server.getDictName())) {
                throw new BaseException(String.format("%s字典名称已存在!", server.getDictName()));
            }
        }
        server.setUpdateTime(new Date());
        updateById(server);
        return server;
    }

    /**
     * 删除角色
     *
     * @param serverId 角色ID
     * @return
     */
    @Override
    public void remove(Long serverId) {
        if (serverId == null) {
            return;
        }
        SystemDict server = getById(serverId);
//        if (server != null && server.getIsPersist().equals(SystemConstants.ENABLED)) {
//            throw new BaseException(String.format("默认数据,禁止删除"));
//        }
        // getCountByRoleId(serverId);
        int count = 0;//
        if (count > 0) {
            throw new BaseException("该角色下存在授权人员,禁止删除!");
        }
        removeById(serverId);
    }

    /**
     * 检测角色编码是否存在
     *
     * @param dictName
     * @return
     */
    @Override
    public Boolean isExist(String dictName) {
        if (StringUtil.isBlank(dictName)) {
            throw new BaseException("serverName不能为空!");
        }
        QueryWrapper<SystemDict> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(SystemDict::getDictName, dictName);
        return count(queryWrapper) > 0;
    }

}
