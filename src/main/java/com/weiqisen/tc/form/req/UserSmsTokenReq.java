package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-25 15:06
 */
@Data
public class UserSmsTokenReq {

    private String phone;

    private String code;

    private String type;

    private String loginType;

    private String appId;
}
