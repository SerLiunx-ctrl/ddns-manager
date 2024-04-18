package com.serliunx.ddns.boot;

import com.serliunx.ddns.config.SystemConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.serliunx.ddns.core.constant.SystemConstants.USER_DIR;

/**
 * 系统初始化
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@Component
public final class SystemInitializer implements CommandLineRunner {

    private final SystemConfiguration systemConfiguration;
    private final DynamicThreadFactory dynamicThreadFactory;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public SystemInitializer(SystemConfiguration systemConfiguration, DynamicThreadFactory dynamicThreadFactory) {
        this.systemConfiguration = systemConfiguration;
        this.dynamicThreadFactory = dynamicThreadFactory;
    }

    @Override
    public void run(String... args) throws Exception {

        // 解压配置文件到主目录
        releaseConfigFile("application.yml");
        // 初始化实例信息
        init();
        // 初始化线程池
        initScheduledThreadPool();
    }

    @SuppressWarnings("all")
    private void releaseConfigFile(String resourceName) throws IOException {
        // 从类路径下获取资源文件
        ClassPathResource resource = new ClassPathResource(resourceName);

        Path path = Paths.get(USER_DIR + "/" + resourceName);
        // 检查文件是否已存在
        if(Files.exists(path)){
            log.debug("文件 {} 已存在, 无需解压.", resourceName);
            return;
        }

        try (InputStream inputStream = resource.getInputStream()) {
            log.debug("正在解压文件 {} 至路径: {}", resourceName, USER_DIR);
            // 创建输出流，写入文件到指定目录
            OutputStream outputStream = Files.newOutputStream(Paths.get(USER_DIR + "/" + resourceName));
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
        }catch (Exception e){
            log.error("文件 {} 解压失败!, 原因: {}", resourceName, e.getMessage());
        }finally {
            log.debug("文件 {} 解压成功!", resourceName);
        }
    }

    private void init(){
        log.info("实例载入中, 请稍候..");

        // TODO 载入实例

        log.info("服务已启动.");
    }

    private void initScheduledThreadPool() {
        SystemConfiguration.Pool poolSettings = systemConfiguration.getPool();
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(poolSettings.getCorePoolSize(),
                dynamicThreadFactory);

        // 线程池保活
        this.scheduledThreadPoolExecutor.submit(() -> {});

        // 设置关闭钩子线程
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("服务关闭中...");
            scheduledThreadPoolExecutor.shutdown();
            log.info("服务已关闭...");
        }, "ShutDownHook"));
    }
}
