package com.weiqisen.tc.form.req;

import lombok.Data;

import java.util.List;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-09-11 17:16
 */
@Data
public class SystemAccessLogsRemoveReq {
    /**
     * ID
     */
    private List<Long> accessIds;
}
