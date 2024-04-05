package com.serliunx.ddns.boot;

import com.serliunx.ddns.config.SystemConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 主服务逻辑引导类
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@Component
public final class BootStrap {

    private final SystemConfiguration systemConfiguration;
    private final DynamicThreadFactory dynamicThreadFactory;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public BootStrap(SystemConfiguration systemConfiguration, DynamicThreadFactory dynamicThreadFactory) {
        this.systemConfiguration = systemConfiguration;
        this.dynamicThreadFactory = dynamicThreadFactory;
    }

    @EventListener
    public void init(ContextRefreshedEvent event){
        SystemConfiguration.Pool poolSettings = systemConfiguration.getPool();
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(poolSettings.getCorePoolSize(), dynamicThreadFactory);
        scheduledThreadPoolExecutor.execute(() -> {
            log.info("服务已启动.");
        });

        //设置关闭钩子线程
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("服务关闭中...");
            scheduledThreadPoolExecutor.shutdown();
            log.info("服务已关闭...");
        }, "ShutDownHook"));
    }
}
