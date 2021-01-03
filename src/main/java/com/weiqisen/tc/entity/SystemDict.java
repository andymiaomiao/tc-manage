package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-28 15:03
 */
@Data
@EqualsAndHashCode()
@NoArgsConstructor
@TableName("system_dict")
public class SystemDict extends AbstractEntity {
    private static final long serialVersionUID = 519732628543375591L;
    /**
     * 服务ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long dictId;
    private Long parentId;
    private String dictType;
    private String dictName;
    private String dictCode;
    private String dictValue;
    private String isFixed;
    private String status;


}
