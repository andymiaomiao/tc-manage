package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author 超级管理员
 * @date 2020-12-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("system_fixed_info")
@ApiModel(value = "SystemFixedInfo对象", description = "")
public class SystemFixedInfo extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "fixed_info_id", type = IdType.ID_WORKER)
    private Long fixedInfoId;

    private Long fixedId;

    private String fixedUser;

    private String fixedPassword;

    private String fixedData;


}
