package com.serliunx.ddns;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * 程序入口
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class DDNSManager {
    public static void main(String[] args) {
        SpringApplication.run(DDNSManager.class);
    }
}