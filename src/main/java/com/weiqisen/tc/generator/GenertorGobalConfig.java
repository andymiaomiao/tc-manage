package com.weiqisen.tc.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-09-10 09:26
 */
@Data
public class GenertorGobalConfig {
    private String entityName;
    private String mapperName;
    private String xmlName;
    private String serviceName;
    private String serviceImplName;
    private String controllerName;
    private IdType idType;
}
