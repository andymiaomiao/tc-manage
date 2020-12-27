package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author weiqisen
 * @date 24/12/2020 5:04 下午
 */
@Data
public class SystemUserInfoReq {
    private Long userId;
    private String nickName;
    private String userName;
    private String password;
    private String userType;
    private Integer status;
    private String email;
    private String mobile;
    private String avatar;
    private String userDesc;
}
