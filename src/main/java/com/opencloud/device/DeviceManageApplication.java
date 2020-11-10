package com.opencloud.device;

import com.opencloud.device.socket.SocketSaveServer;
import com.opencloud.device.socket.SocketTraceServer;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * 系统基础服务
 * 提供系统用户、权限分配、资源、客户端管理
 *
 * @author liuyadu
 */
@SpringBootApplication
@MapperScan(basePackages = "com.opencloud.**.mapper")
public class DeviceManageApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(DeviceManageApplication.class, args);
        applicationContext.getBean(SocketSaveServer.class).start();//在spring容器启动后，取到已经初始化的SocketServer，启动Socket服务
        applicationContext.getBean(SocketTraceServer.class).start();//在spring容器启动后，取到已经初始化的SocketServer，启动Socket服务
    }
}
