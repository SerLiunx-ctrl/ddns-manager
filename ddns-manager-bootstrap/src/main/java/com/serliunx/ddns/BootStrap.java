package com.serliunx.ddns;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
@MapperScan("com.serliunx.ddns.core.instance.sql.mapper")
public class BootStrap {
    public static void main(String[] args) {
        SpringApplication.run(BootStrap.class, args);
    }
}
