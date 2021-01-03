package com.weiqisen.tc.generator;

import lombok.Data;

import java.util.Map;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-09-09 17:21
 */
@Data
public class GenertorPackageConfig {

    /**
     * 实体类包名
     */
    private String entity = "entity";
    /**
     * service包名
     */
    private String service = "service";
    /**
     * serviceImpl包名
     */
    private String serviceImpl = "service.impl";
    /**
     * mapper包名
     */
    private String mapper = "mapper";
    /**
     * xml包名
     */
    private String xml = "mapper";
    /**
     * controller包名
     */
    private String controller = "product";
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
}
