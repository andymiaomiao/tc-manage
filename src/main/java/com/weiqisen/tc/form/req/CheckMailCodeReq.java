package com.weiqisen.tc.form.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author weiqisen
 * @date 23/11/2020 10:20 AM
 */
@Data
public class CheckMailCodeReq {

    @ApiModelProperty(value = "邮箱号", name = "邮箱号", required = true, example = "")
    private String mail;

    @ApiModelProperty(value = "验证码", name = "验证码", required = true, example = "")
    private String mailCode;
}
