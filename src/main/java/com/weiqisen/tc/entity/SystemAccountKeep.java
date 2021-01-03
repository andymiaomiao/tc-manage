package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-08-10 15:04
 */
@Data
@NoArgsConstructor
@TableName("system_account_keep")
public class SystemAccountKeep extends AbstractEntity {
    @TableId(type = IdType.ID_WORKER)
    private Long keepId;
    private Long tenantId;
    private String keepName;
    private String domain;
    private String url;
    private String port;
    private String path;
    private String type;
    private String category;
    private String account;
    private String password;
    private Integer sort;
    private Integer status;
}
