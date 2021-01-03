//package com.weiqisen.tc.generator;
//
//import lombok.Data;
//
//import java.io.Serializable;
//
///**
// * @author jihao
// * @version 1.0
// * @date 2020-09-04 16:27
// */
//@Data
//public class GenerateConfig implements Serializable {
//
//    private Long genertorId;
//    /**
//     * 模块名称
//     */
//    private String moduleName;
//
//    /**
//     * 代码生成的类的父包名称
//     */
//    private String parentPackage;
//    /**
//     * 需要生成的表
//     */
//    private String[] include;
//    /**
//     * 需要排除的表
//     */
//    private String[] exclude;
//    /**
//     * 去掉表的前缀
//     */
//    private String[] tablePrefix;
//
//    /**
//     * 代码生成包含的表，可为空，为空默认生成所有
//     */
//    private String[] includeTables;
//    /**
//     * 表名生成策略
//     */
//    private NamingStrategy naming;
//    /**
//     * 字段生成策略
//     */
//    private NamingStrategy columnNaming;
//
//    /**
//     * 生成代码里，注释的作者
//     */
//    private String author;
//
////    /**
////     * 数据库类型
////     */
////    private DbType dbType;
////
////    /**
////     * jdbc驱动
////     */
////    private String jdbcDriver;
////
////    /**
////     * 数据库连接地址
////     */
////    private String jdbcUrl;
////
////    /**
////     * 数据库账号
////     */
////    private String jdbcUserName;
////
////    /**
////     * 数据库密码
////     */
////    private String jdbcPassword;
//
//    /**
//     * 代码生成目录
//     */
//    private String outputDir;
//
//    /**
//     * 数据库配置
//     */
//    private GenertarDataSource genertarDataSource;
//
//    /**
//     * 生成模版
//     */
//    private GenertorTemplateConfig genertorTemplateConfig;
//
//    /**
//     * 包配置
//     */
//    private GenertorPackageConfig genertorPackageConfig;
//
//    private GenertorStrategyConfig genertorStrategyConfig;
//
//    private GenertorGobalConfig genertorGobalConfig;
//
//}
