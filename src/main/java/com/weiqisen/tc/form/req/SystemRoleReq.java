package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author weiqisen
 * @date 24/12/2020 5:04 下午
 */
@Data
public class SystemRoleReq {
    private Long roleId;
    private String roleCode;
    private String roleName;
    private Integer roleType;
    private String roleDesc;
    private Integer status;
}
