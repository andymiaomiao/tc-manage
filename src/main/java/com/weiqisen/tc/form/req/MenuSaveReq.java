package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author weiqisen
 * @date 2/1/2021 1:18 下午
 */
@Data
public class MenuSaveReq {
    private Long apiId;
    private Integer apiType;
    private String apiCode;
    private String apiName;
    private String apiCategory;
    private String requestMethod;
    private String path;
    private Integer status;
    private Integer priority;
    private String apiDesc;
    private Integer isAuth;
}
