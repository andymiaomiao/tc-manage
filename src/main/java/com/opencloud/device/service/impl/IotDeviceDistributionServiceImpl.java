package com.opencloud.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.opencloud.device.entity.IotDevice;
import com.opencloud.device.entity.IotDeviceDistribution;
import com.opencloud.device.exception.BaseException;
import com.opencloud.device.mapper.IotDeviceDistributionMapper;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.mybatis.base.service.impl.BaseServiceImpl;
import com.opencloud.device.service.IotDeviceDistributionService;
import com.opencloud.device.service.IotDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class IotDeviceDistributionServiceImpl extends BaseServiceImpl<IotDeviceDistributionMapper, IotDeviceDistribution> implements IotDeviceDistributionService {

    @Autowired
    private IotDeviceService iotDeviceService;

//    @Autowired
//    private SystemActionService systemActionService;

//    @Value("${spring.application.name}")
//    private String DEFAULT_SERVICE_ID;
//
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    @Override
    public IPage<IotDeviceDistribution> findPage(PageParams pageParams) {
        IotDeviceDistribution query = pageParams.mapToBean(IotDeviceDistribution.class);
        QueryWrapper<IotDeviceDistribution> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .like(ObjectUtils.isNotEmpty(query.getDistributionName()), IotDeviceDistribution::getDistributionName, query.getDistributionName())
                .orderByDesc(IotDeviceDistribution::getCreateTime);
        return baseMapper.selectPage(pageParams, queryWrapper);
    }

    /**
     * 查询列表
     *
     * @return
     */
    @Override
    public List<IotDeviceDistribution> findList(RequestParams requestParams) {
        IotDeviceDistribution query = requestParams.mapToBean(IotDeviceDistribution.class);
        QueryWrapper<IotDeviceDistribution> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .orderByDesc(IotDeviceDistribution::getCreateTime);
        return list(queryWrapper);
    }

    /**
     * 检查菜单编码是否存在
     *
     * @param distributionName
     * @return
     */
    @Override
    public Boolean isExist(String distributionName) {
        QueryWrapper<IotDeviceDistribution> queryWrapper = new QueryWrapper();
        queryWrapper.lambda()
                .eq(IotDeviceDistribution::getDistributionName, distributionName);
        int count = count(queryWrapper);
        return count > 0 ? true : false;
    }

    /**
     * 添加菜单资源
     *
     * @param deviceDistribution
     * @return
     */
    @Override
    public IotDeviceDistribution add(IotDeviceDistribution deviceDistribution) {
        if (isExist(deviceDistribution.getDistributionName())) {
            throw new BaseException(String.format("%s名称已存在!", deviceDistribution.getDistributionName()));
        }
        deviceDistribution.setCreateTime(new Date());
        deviceDistribution.setUpdateTime(deviceDistribution.getCreateTime());
        save(deviceDistribution);
        // 同步权限表里的信息
        return deviceDistribution;
    }

    /**
     * 修改菜单资源
     *
     * @param deviceDistribution
     * @return
     */
    @Override
    public IotDeviceDistribution update(IotDeviceDistribution deviceDistribution) {
        IotDeviceDistribution saved = getById(deviceDistribution.getDistributionId());
        if (saved == null) {
            throw new BaseException(String.format("%s信息不存在!", deviceDistribution.getDistributionId()));
        }
        if (!saved.getDistributionName().equals(deviceDistribution.getDistributionName())) {
            // 和原来不一致重新检查唯一性
            if (isExist(deviceDistribution.getDistributionName())) {
                throw new BaseException(String.format("%s名称已存在!", deviceDistribution.getDistributionName()));
            }
        }
        deviceDistribution.setUpdateTime(new Date());
        updateById(deviceDistribution);
        return deviceDistribution;
    }


    /**
     * 移除菜单
     *
     * @param distributionId
     * @return
     */
    @Override
    public void remove(Long distributionId) {
        QueryWrapper<IotDevice> queryWrapper = new QueryWrapper<>();
        // 查询该地址是否有设备使用过
        queryWrapper.lambda()
                .eq(IotDevice::getDistribuId,distributionId);
        List<IotDevice> list = iotDeviceService.list(queryWrapper);
        if(list!=null&&list.size()>0){
            throw new BaseException(String.format("分布地已被使用无法删除"));
        }
        removeById(distributionId);
    }
//
//    @Override
//    public List<IotDeviceDistribution> findMenuByUser(Map<String,Object> paramMap) {
////        Map<String,Object> paramMap = new HashMap<>(16);
////        paramMap.put("userId",userId);
////        paramMap.put("systemCode",systemCode);
//        List<IotDeviceDistribution> list = baseMapper.findMenuByUser(paramMap);
////        QueryWrapper<IotDeviceDistribution> queryWrapper = new QueryWrapper();
////        System.out.println("中饭呢哦跟首发发飞机饿哦戢国鹏"+query.getParentId());
////        queryWrapper.lambda()
////                .eq(ObjectUtils.isNotEmpty(query.getParentId()), IotDeviceDistribution::getParentId, query.getParentId());
////        List<IotDeviceDistribution> list = list(queryWrapper);
//        //根据优先级从大到小排序
//        list.sort((IotDeviceDistribution h1, IotDeviceDistribution h2) -> h2.getPriority().compareTo(h1.getPriority()));
//        return list;
//    }
}
