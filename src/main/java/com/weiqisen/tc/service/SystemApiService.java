package com.weiqisen.tc.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.weiqisen.tc.entity.SystemApi;
import com.weiqisen.tc.form.req.SystemApiBatchUpdateReq;
import com.weiqisen.tc.model.PageParams;
import com.weiqisen.tc.mybatis.base.service.IBaseService;

import java.util.List;
import java.util.Map;

/**
 * 接口资源管理
 *
 * @author liuyadu
 */
public interface SystemApiService extends IBaseService<SystemApi> {
    /**
     * 分页查询
     *
     * @param pageParams
     * @return
     */
    IPage<SystemApi> findPage(PageParams pageParams);

    /**
     * 查询列表
     *
     * @return
     */
    List<SystemApi> findList(String serviceId);

    Map findActionList(String actionId, Integer actionType);

    Map findClientList(String clientId, Integer actionType);

    /**
     * 检查接口编码是否存在
     *
     * @param apiCode
     * @return
     */
    Boolean isExist(String apiCode);

    /**
     * 添加接口
     *
     * @param api
     * @return
     */
    void add(SystemApi api);

    /**
     * 修改接口
     *
     * @param api
     * @return
     */
    void update(SystemApi api);

    /**
     * 查询接口
     *
     * @param apiCode
     * @return
     */
    SystemApi getByCode(String apiCode);

    /**
     * 移除接口
     *
     * @param apiId
     * @return
     */
    void remove(Long apiId);

    /**
     * 功能按钮绑定API
     *
     * @param actionId
     * @param apiIds
     */
    void addApiAction(Long actionId, String... apiIds);

    /**
     * 获取等待更新的服务器列表
     * @return
     */
    List<Map> getBatchUpdateServiceList();

    void batchUpdateServiceApi(SystemApiBatchUpdateReq systemApiBatchUpdateReq);

    /**
     * 更新资源服务器
     *
     * @param serviceIds
     * @return
     */
    boolean batchUpdateService(String ...serviceIds);


    Map findMenuApiList(Long menuId, Integer apiType);
}
