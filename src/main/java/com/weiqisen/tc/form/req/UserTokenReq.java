package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-25 15:06
 */
@Data
public class UserTokenReq {

    private String username;

    private String password;

    private String type;

    private String loginType;

    private String code;

    private String appId;
}
