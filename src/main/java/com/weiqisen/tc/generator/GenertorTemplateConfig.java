package com.weiqisen.tc.generator;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-09-09 17:02
 */
@Data
public class GenertorTemplateConfig {
    private String entity = "/templates/entity.java.vm";
    private String entityKt = "/templates/entity.kt";
    private String service = "/templates/service.java.vm";
    private String serviceImpl = "/templates/serviceImpl.java.vm";
    private String mapper = "/templates/mapper.java.vm";
    private String xml = "/templates/mapper.xml.vm";
    private String controller = "/templates/controller.java.vm";

    private Boolean isEntity = true;
    private Boolean isEntityKt = true;
    private Boolean isService = true;
    private Boolean isServiceImpl = true;
    private Boolean isMapper = true;
    private Boolean isXml = true;
    private Boolean isController = true;

}
