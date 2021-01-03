package com.weiqisen.tc.form.req;

import lombok.Data;

/**
 * @author weiqisen
 * @date 24/12/2020 5:04 下午
 */
@Data
public class SystemMenuSaveReq {
    private Long menuId;
    private String menuCode;
    private String menuName;
    private Integer menuType;
    private Integer type;
    private String icon;
    private Integer level;
    private String scheme;
    private String path;
    private String target;
    private Integer status;
    private Long parentId;
    private Integer priority;
    private String menuDesc;
}
