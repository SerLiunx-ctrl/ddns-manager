package com.serliunx.ddns.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * 系统参数配置
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@ConfigurationProperties("system")
public class SystemConfiguration {

    /**
     * 线程池配置
     */
    private Pool pool;

    @Getter
    @Setter
    public static class Pool{

        /**
         * 核心线程数量
         */
        private Integer corePoolSize;
        /**
         * 线程名称模板
         * <li> 请保留编码关键字%s
         */
        private String namePattern = "task-%s";
    }
}
