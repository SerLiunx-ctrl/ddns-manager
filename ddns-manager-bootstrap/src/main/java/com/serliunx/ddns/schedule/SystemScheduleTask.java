package com.serliunx.ddns.schedule;

import com.serliunx.ddns.client.IPAddressClient;
import com.serliunx.ddns.context.SystemContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 系统通用的定时任务: 目前仅定时获取本机公网IP
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@Component
public class SystemScheduleTask {

    private final IPAddressClient ipAddressClient;
    private final SystemContext systemContext;

    public SystemScheduleTask(IPAddressClient ipAddressClient, SystemContext systemContext) {
        this.ipAddressClient = ipAddressClient;
        this.systemContext = systemContext;
    }

    @Scheduled(fixedDelay = 300, timeUnit = TimeUnit.SECONDS)
    private void getPublicIp(){
        systemContext.setNewestIp(false);
        String publicIp = ipAddressClient.getIPAddress().getQuery();
        log.debug("获取到本机公网IP: {}", publicIp);
        systemContext.setNewestIp(true);
        systemContext.setPublicIp(publicIp);
    }
}
