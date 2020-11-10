package com.opencloud.device.form.req;

import lombok.Data;

import java.util.List;

@Data
public class UserReq {
    /**
     * 企业ID
     */
    private Long tenantId;

    private List<Long> userIds;

    private Integer type;

    private String userName;

    private String loginType;

    private String thirdParty;

}
