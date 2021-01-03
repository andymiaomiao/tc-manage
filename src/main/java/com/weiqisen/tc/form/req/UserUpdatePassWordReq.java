package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-27 11:21
 */
@Data
public class UserUpdatePassWordReq {

    private String oldPassword;
    private String newPassword;
}
