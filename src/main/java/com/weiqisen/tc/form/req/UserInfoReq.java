package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-27 11:23
 */
@Data
public class UserInfoReq {
    private String nickName;
    private String mobile;
    private String email;
    private String city;
    private String userDesc;
    private String avatar;
}
