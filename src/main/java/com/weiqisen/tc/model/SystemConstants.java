package com.weiqisen.tc.model;

/**
 * 通用权限常量
 *
 * @author weiqisen
 */
public class SystemConstants {

    /**
     * 服务名称
     */
    public static final String SYSTEM_SERVER = "system-service";

    /**
     * 状态:0-无效 1-有效
     */
    public final static int ENABLED = 1;
    public final static int DISABLED = 0;



    /**
     * 系统用户类型:超级管理员-super 普通管理员-admin
     */
    public final static String USER_TYPE_SUPER = "super";
    public final static String USER_TYPE_ADMIN = "admin";

    /**
     * 账号状态
     * 0:禁用、1:正常、2:锁定
     */
    public final static int ACCOUNT_STATUS_DISABLE = 0;
    public final static int ACCOUNT_STATUS_NORMAL = 1;
    public final static int ACCOUNT_STATUS_LOCKED = 2;

    /**
     * 账号类型:
     * username:系统用户名、email：邮箱、mobile：手机号、qq：QQ号、weixin：微信号、weibo：微博
     */
    public final static String ACCOUNT_TYPE_USERNAME = "username";
    public final static String ACCOUNT_TYPE_EMAIL = "email";
    public final static String ACCOUNT_TYPE_MOBILE = "mobile";

    /**
     * 账号域
     */
    public static final String ACCOUNT_DOMAIN_ADMIN = "@admin.com";
    public static final String ACCOUNT_DOMAIN_SITE= "@site.com";

    /**
     * 账号类型
     */
    public static final String ACCOUNT_ADMIN_TYPE = "1";
    public static final String ACCOUNT_SUPPLIER_TYPE= "10";
    public static final String ACCOUNT_FACILITATOR_TYPE = "20";
    public static final String ACCOUNT_PURCHASINGR_TYPE= "30";
    public static final String ACCOUNT_REPERTORY_TYPE= "40";
    public static final String ACCOUNT_AGENT_TYPE= "50";

}
