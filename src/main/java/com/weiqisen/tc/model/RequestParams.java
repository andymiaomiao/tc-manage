package com.weiqisen.tc.model;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-07-28 16:42
 */
public class RequestParams {

    private Map<String, Object> requestMap = Maps.newHashMap();

    public RequestParams(Map map) {
        if (map == null) {
            map = Maps.newHashMap();
        }
        requestMap.putAll(map);
    }

    public <T> T mapToBean(Class<T> t) {
        return BeanUtil.mapToBean(this.requestMap, t,true);
    }

    public Map<String, Object> getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(Map<String, Object> requestMap) {
        this.requestMap = requestMap;
    }
}
