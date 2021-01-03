package com.weiqisen.tc.model;

/**
 * @author weiqisen
 */
public class CommonConstants {
    /**
     * 默认超级管理员账号
     */
    public final static String ROOT = "admin";

    /**
     * 默认最小页码
     */
    public static final int MIN_PAGE = 0;
    /**
     * 最大显示条数
     */
    public static final int MAX_LIMIT = 999;
    /**
     * 默认页码
     */
    public static final int DEFAULT_PAGE = 1;
    /**
     * 默认显示条数
     */
    public static final int DEFAULT_LIMIT = 10;
    /**
     * 页码 KEY
     */
    public static final String PAGE_KEY = "pageNo";
    /**
     * 显示条数 KEY
     */
    public static final String PAGE_SIZE_KEY = "pageSize";
    /**
     * 排序字段 KEY
     */
    public static final String PAGE_SORT_KEY = "sort";
    /**
     * 排序方向 KEY
     */
    public static final String PAGE_ORDER_KEY = "order";

    /**
     * 客户端ID KEY
     */
    public static final String SIGN_API_KEY = "ApiKey";

    /**
     * 客户端秘钥 KEY
     */
    public static final String SIGN_SECRET_KEY = "SecretKey";

    /**
     * 随机字符串 KEY
     */
    public static final String SIGN_NONCE_KEY = "Nonce";
    /**
     * 时间戳 KEY
     */
    public static final String SIGN_TIMESTAMP_KEY = "Timestamp";
    /**
     * 签名类型 KEY
     */
    public static final String SIGN_SIGN_TYPE_KEY = "SignType";
    /**
     * 签名结果 KEY
     */
    public static final String SIGN_SIGN_KEY = "Sign";

    /**
     * 资源扫描
     */
    public static final String API_RESOURCE = "cloud_api_resource";

    /**
     * 访问日志
     */
    public static final String API_ACCESS_LOGS = "cloud_api_access_logs";

    /**
     * 登录日志
     */
    public static final String ACCOUNT_LOGS = "cloud_account_logs";

    public static final int CLOUD_HEALTH_MANAGE_TYPE = 20;

    public static final int DEVICE_MANAGE_TYPE = 10;

    public static final int ADMIN_MANAGE_TYPE = 1;



    /**
     * 管理端
     */


    public static final String DEVICE_MANAGE_NAME = "device-manage";

    public static final String ADMIN_MANAGE_NAME = "admin-manage";

}
