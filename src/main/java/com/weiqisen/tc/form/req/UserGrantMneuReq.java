package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-10-22 17:45
 */
@Data
public class UserGrantMneuReq {
    private Long userId;
    private String menuIds;
}
