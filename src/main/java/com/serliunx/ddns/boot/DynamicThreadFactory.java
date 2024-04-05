package com.serliunx.ddns.boot;

import com.serliunx.ddns.config.SystemConfiguration;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Component
public final class DynamicThreadFactory implements ThreadFactory {

    private final AtomicInteger threadIndex = new AtomicInteger();
    private final SystemConfiguration systemConfiguration;

    public DynamicThreadFactory(SystemConfiguration systemConfiguration) {
        this.systemConfiguration = systemConfiguration;
    }

    @Override
    public Thread newThread(Runnable r) {
        SystemConfiguration.Pool poolSettings = systemConfiguration.getPool();
        return new Thread(r, String.format(poolSettings.getNamePattern(), threadIndex.getAndIncrement()));
    }
}
