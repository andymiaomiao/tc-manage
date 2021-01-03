package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author weiqisen
 * @date 2/1/2021 1:27 下午
 */
@Data
public class ApiActionGrantReq {
    private Long actionId;
    private String apiIds;
}
