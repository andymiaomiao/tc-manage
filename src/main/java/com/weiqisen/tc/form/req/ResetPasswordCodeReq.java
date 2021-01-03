package com.weiqisen.tc.form.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author weiqisen
 * @date 23/11/2020 10:20 AM
 */
@Data
public class ResetPasswordCodeReq {

    @ApiModelProperty(value = "手机号", name = "手机号", required = true, example = "")
    private String phoneNum;

    @ApiModelProperty(value = "验证码", name = "验证码", required = true, example = "")
    private String smsCode;
}
