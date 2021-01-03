package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-09-10 11:17
 */
@Data
public class GenertorExeuteReq {
    private String genertorId;
    private String xml;
    private String xmlName;
    private String xmlTemp;
    private String mapperName;
    private String mapper;
    private String superMapperClass;
    private String mapperTemp;
    private String controllerName;
    private String controller;
    private String superControllerClass;
    private String controllerTemp;
    private String serviceImplName;
    private String serviceImpl;
    private String superServiceImplClass;
    private String serviceImplTemp;
    private String serviceName;
    private String service;
    private String superServiceClass;
    private String serviceTemp;
    private String entityName;
    private String entity;
    private String superEntityClass;
    private String entityTemp;
    private String parentPackage;
    private String moduleName;
    private String tablePrefix;
    private String naming;
    private String idType;
    private String columnNaming;


}
