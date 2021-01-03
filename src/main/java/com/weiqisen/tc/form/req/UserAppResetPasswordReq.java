package com.weiqisen.tc.form.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-27 11:21
 */
@Data
public class UserAppResetPasswordReq {

    @ApiModelProperty(value = "新密码", name = "新密码",required = true,example = "")
    private String newPassword;

    private Long userId;

}
