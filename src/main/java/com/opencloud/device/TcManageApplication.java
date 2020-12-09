package com.opencloud.device;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author weiqisen
 * @date 9/12/2020 9:51 上午
 */
@SpringBootApplication
@MapperScan(basePackages = "com.opencloud.**.mapper")
public class TcManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(TcManageApplication.class, args);
    }
}
