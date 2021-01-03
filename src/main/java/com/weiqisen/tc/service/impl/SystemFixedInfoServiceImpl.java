package com.weiqisen.tc.service.impl;


import com.weiqisen.tc.entity.SystemFixedInfo;
import com.weiqisen.tc.mapper.SystemFixedInfoMapper;
import com.weiqisen.tc.mybatis.base.service.impl.BaseServiceImpl;
import com.weiqisen.tc.service.SystemFixedInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author 超级管理员
 * @date 2020-12-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SystemFixedInfoServiceImpl extends BaseServiceImpl<SystemFixedInfoMapper, SystemFixedInfo> implements SystemFixedInfoService {

}
