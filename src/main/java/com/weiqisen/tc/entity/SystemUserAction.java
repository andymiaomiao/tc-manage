package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统角色-角色与用户关联
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Data
@EqualsAndHashCode()
@NoArgsConstructor
@TableName("system_user_action")
public class SystemUserAction {
    private static final long serialVersionUID = -667816444278087761L;
    /**
     * 操作权限ID
     */
    @TableId(type= IdType.INPUT)
    private Long actionId;

    /**
     * 用户ID
     */
    private Long userId;
}
