package com.weiqisen.tc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.weiqisen.tc.entity.TcDevice;
import com.weiqisen.tc.exception.BaseException;
import com.weiqisen.tc.form.res.TcDeviceRes;
import com.weiqisen.tc.mapper.TcDeviceMapper;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.service.TcDeviceService;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.Map;

/**
 * @author weiqisen
 * @date 1/12/2020 6:13 下午
 */
@Service
public class TcDeviceServiceImpl extends BaseServiceImpl<TcDeviceMapper, TcDevice> implements TcDeviceService {


    @Override
    public IPage<TcDeviceRes> findPage(PageParams pageParams) {
        TcDevice query = pageParams.mapToBean(TcDevice.class);
        QueryWrapper<TcDevice> queryWrapper = new QueryWrapper();
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    @Override
    public TcDeviceRes count(Map<String, Object> map) {
        return null;
    }

    @Override
    public TcDevice add(TcDevice tcDevice) {
        tcDevice.setCreateTime(new Date());
        tcDevice.setUpdateTime(tcDevice.getCreateTime());
        save(tcDevice);
        return tcDevice;
    }

    @Override
    public Boolean update(TcDevice tcDevice) {
        TcDevice saved = getById(tcDevice.getDeviceId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在!", tcDevice.getDeviceId()));
        }
        tcDevice.setUpdateTime(new Date());
        return updateById(tcDevice);
    }

}
