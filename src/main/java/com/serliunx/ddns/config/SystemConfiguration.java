package com.serliunx.ddns.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    private Feign feign;

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

    @Getter
    @Setter
    public static class Feign{
        /**
         * Feign客户端的扫描路径
         * <li> 使用默认即可
         */
        private String basicPackages;
    }
}
