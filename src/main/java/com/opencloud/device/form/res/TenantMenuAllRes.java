package com.opencloud.device.form.res;

import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-29 17:46
 */
@Data
public class TenantMenuAllRes {
    private Long menuId;
    private String menuName;
    private Long parentId;
    private Integer level;
    private Integer sort;
}
