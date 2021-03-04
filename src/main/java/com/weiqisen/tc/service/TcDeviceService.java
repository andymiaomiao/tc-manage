package com.weiqisen.tc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import com.weiqisen.tc.entity.TcDevice;
import com.weiqisen.tc.form.res.TcDeviceRes;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;

import java.util.Map;


/**
 * @author weiqisen
 * @date 1/12/2020 6:13 下午
 */
public interface TcDeviceService extends IBaseService<TcDevice> {

    IPage<TcDeviceRes> findPage(PageParams pageParams);

    TcDevice add(TcDevice waterPlantDevice);

    Boolean update(TcDevice waterPlantDevice);

    TcDeviceRes count(Map<String, Object> map);

}
