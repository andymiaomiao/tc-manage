package com.weiqisen.tc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.weiqisen.tc.mybatis.base.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 系统资源-功能操作
 *
 * @author: liuyadu
 * @date: 2018/10/24 16:21
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("system_genertor")
public class SystemGenertor extends AbstractEntity {
    private static final long serialVersionUID = 1471599074044557390L;
    /**
     * 资源ID
     */
    @TableId(type= IdType.ID_WORKER)
    private Long genertorId;

    /**
     * 连接名称
     */
    private String lineName;
    /**
     * 类型
     */
    private String type;

    /**
     * 驱动
     */
    private String driverName;

    /**
     * url
     */
    private String url;

    /**
     * port
     */
    private String port;

    /**
     * schemas
     */
    @TableField("`schemas`")
    private String schemas;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
