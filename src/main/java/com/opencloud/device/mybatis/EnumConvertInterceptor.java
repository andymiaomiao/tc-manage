package com.opencloud.device.mybatis;

/**
 * @author LYD
 */
public interface EnumConvertInterceptor {
    boolean convert(EntityMap map, String key, Object v);
}
