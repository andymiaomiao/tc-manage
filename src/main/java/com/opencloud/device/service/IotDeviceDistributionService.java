package com.opencloud.device.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.opencloud.device.entity.IotDeviceDistribution;
import com.opencloud.device.model.PageParams;
import com.opencloud.device.model.RequestParams;
import com.opencloud.device.mybatis.base.service.IBaseService;

import java.util.List;

/**
 * 菜单资源管理
 * @author liuyadu
 */
public interface IotDeviceDistributionService extends IBaseService<IotDeviceDistribution> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<IotDeviceDistribution> findPage(PageParams pageParams);
//
    /**
     * 查询列表
     * @return
     */
    List<IotDeviceDistribution> findList(RequestParams requestParams);

    /**
     * 检查菜单编码是否存在
     *
     * @param distributionName
     * @return
     */
    Boolean isExist(String distributionName);


    /**
     * 添加菜单资源
     *
     * @param deviceDistribution
     * @return
     */
    IotDeviceDistribution add(IotDeviceDistribution deviceDistribution);

    /**
     * 修改菜单资源
     *
     * @param deviceDistribution
     * @return
     */
    IotDeviceDistribution update(IotDeviceDistribution deviceDistribution);

    /**
     * 移除菜单
     *
     * @param distributionId
     * @return
     */
    void remove(Long distributionId);

}
