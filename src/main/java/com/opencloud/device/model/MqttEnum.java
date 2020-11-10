package com.opencloud.device.model;

/**
 * @author jihao
 * @version 1.0
 * @date 2020-07-20 16:06
 */
public enum MqttEnum {
    /**
     * 自定义的状态码
     */
    DEVICE_ACK(1,"ACK","可执行"),
    DEVICE_NAK(2,"NAK","无法执行"),
    DEVICE_WAIT(3,"WAIT","等待执行"),
    DEVICE_ACT(4,"ACT","执行中"),
    DEVICE_SUC(5,"SUC","执行成功"),
    DEVICE_FAIL(6,"FAIL","执行失败");
    /**
     * 错误码
     */
    public Integer status;
    /**
     * 提示代码
     */
    public String code;
    /**
     * 提示信息
     */
    public String message;

    /**
     * 构造函数
     */
    MqttEnum(Integer status, String code, String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码
     */
    public String getCode(){
        return code;
    }
    /**
     * 获取提示信息
     */
    public String getMessage(){
        return message;
    }

}
