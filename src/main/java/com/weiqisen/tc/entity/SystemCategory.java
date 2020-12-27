package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author weiqisen
 * @version 1.0
 * @date 2020-08-10 15:04
 */
@Data
@NoArgsConstructor
@TableName("system_category")
public class SystemCategory extends AbstractEntity {
    @TableId(type = IdType.ID_WORKER)
    private Long categoryId;
    private Long tenantId;
    private Long parentId;
    private Integer categoryType;
    private Integer level;
    private String name;
    private Integer status;
    private Integer sort;
}
