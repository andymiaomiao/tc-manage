package com.weiqisen.tc.form.req;

import com.weiqisen.tc.entity.SystemApi;
import lombok.Data;

import java.util.List;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-11-02 19:18
 */
@Data
public class SystemApiBatchUpdateReq {
    List<SystemApi> systemApiList;
}
