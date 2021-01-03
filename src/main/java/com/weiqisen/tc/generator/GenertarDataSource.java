package com.weiqisen.tc.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-09-09 17:00
 */
@Data
public class GenertarDataSource {
    /**
     * 数据库类型
     */
    private DbType dbType;

    /**
     * jdbc驱动
     */
    private String jdbcDriver;

    /**
     * 数据库连接地址
     */
    private String jdbcUrl;

    /**
     * 数据库账号
     */
    private String jdbcUserName;

    /**
     * 数据库密码
     */
    private String jdbcPassword;
}
