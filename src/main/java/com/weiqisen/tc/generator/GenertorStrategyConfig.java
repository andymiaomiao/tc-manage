package com.weiqisen.tc.generator;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-09-09 17:01
 */
@Data
public class GenertorStrategyConfig {

    /**
     * 实体父类class引用路径
     */
    private String superEntityClass;
    /**
     * 表中哪些字段放到父类中
     */
    private String[] superEntityColumns;
    /**
     * mapper父类class引用路径
     */
    private String superMapperClass;
    /**
     * service父类class引用路径
     */
    private String superServiceClass;
    /**
     * serviceImpl父类class引用路径
     */
    private String superServiceImplClass;
    /**
     * controller父类class引用路径
     */
    private String superControllerClass;

}
