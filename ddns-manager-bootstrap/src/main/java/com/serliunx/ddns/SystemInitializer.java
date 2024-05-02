package com.serliunx.ddns;

import com.serliunx.ddns.api.constant.SystemConstants;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.config.SystemConfiguration;
import com.serliunx.ddns.core.DefaultInstanceContext;
import com.serliunx.ddns.core.StatefulInstance;
import com.serliunx.ddns.core.instance.factory.JsonFileInstanceFactory;
import com.serliunx.ddns.core.instance.factory.XmlFileInstanceFactory;
import com.serliunx.ddns.core.instance.factory.YamlFileInstanceFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.serliunx.ddns.api.constant.SystemConstants.USER_DIR;

/**
 * 系统初始化
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@Component
public final class SystemInitializer implements CommandLineRunner{

    private final SystemConfiguration systemConfiguration;
    private final DynamicThreadFactory dynamicThreadFactory;
    private final DefaultInstanceContext instanceContext;

    /**
     * 正在运行的实例信息
     */
    @Getter
    private final Map<String, StatefulInstance> runningInstance = new ConcurrentHashMap<>();

    /**
     * 运行失败/已停止的实例
     */
    @Getter
    private final Map<String, StatefulInstance> stoppedInstance = new ConcurrentHashMap<>();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private Set<Instance> instances;

    public SystemInitializer(SystemConfiguration systemConfiguration, DynamicThreadFactory dynamicThreadFactory,
                             DefaultInstanceContext instanceContext) {
        this.systemConfiguration = systemConfiguration;
        this.dynamicThreadFactory = dynamicThreadFactory;
        this.instanceContext = instanceContext;
    }

    @Override
    public void run(String... args) throws Exception {
        // 解压配置文件到主目录
        releaseFile("application.yml");
        // 解压数据库文件到主目录
        releaseFile("ddns_data.db");
        // 初始化实例信息
        init();
        // 初始化线程池
        initScheduledThreadPool();
        // 运行实例
        runInstances();
    }

    @SuppressWarnings("all")
    private void releaseFile(String resourceName) throws IOException {
        // 从类路径下获取资源文件
        ClassPathResource resource = new ClassPathResource(resourceName);

        Path path = Paths.get(USER_DIR + File.separator + resourceName);
        // 检查文件是否已存在
        if(Files.exists(path)){
            log.debug("文件 {} 已存在, 无需解压.", resourceName);
            return;
        }

        try (InputStream inputStream = resource.getInputStream()) {
            log.debug("正在解压文件 {} 至路径: {}", resourceName, USER_DIR);
            // 创建输出流，写入文件到指定目录
            OutputStream outputStream = Files.newOutputStream(path);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
        }catch (Exception e){
            log.error("文件 {} 解压失败!, 原因: {}", resourceName, e.getMessage());
        }
    }

    private void init(){
        log.info("实例载入中, 请稍候..");
        // 初始化实例工厂
        initFactories();
        instances = instanceContext.getInstances();
        // 设置实例上下文信息、初始化
        instances.forEach(i -> {
            i.setInstanceContext(instanceContext);
            try {
                log.debug("{}", i);
                i.init();
            }catch (Exception e){
                log.error("实例 {} 加载出现异常, 已撤回加载. 原因 => {}", i.getName(), e.getMessage());
                instances.remove(i);
            }
        });
        log.info("成功载入 {} 个实例", instances.size());
    }

    private void initFactories(){
        // 读取.json文件
        instanceContext.addInstanceFactory(new JsonFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        // 读取.xml文件
        instanceContext.addInstanceFactory(new XmlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        // 读取.yml/.yaml文件
        instanceContext.addInstanceFactory(new YamlFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR));
        // 读取数据库中的文件(暂时搁置相关功能开发)
        // instanceContext.addInstanceFactory(new DatabaseInstanceFactory(instanceService));
        // 刷新容器
        instanceContext.refresh();
    }

    private void initScheduledThreadPool() {
        SystemConfiguration.Pool poolSettings = systemConfiguration.getPool();
        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(poolSettings.getCorePoolSize(),
                dynamicThreadFactory);

        // 线程池保活
        this.scheduledThreadPoolExecutor.submit(() -> {});

        // 设置关闭钩子线程
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("服务正在关闭, 可能需要一定时间.");
            //执行实例的关闭钩子函数
            try{
                instances.forEach(Instance::onClose);
            }catch (Exception e){
                log.error("实例钩子函数执行出现异常 => {}", e.getMessage());
            }finally {
                scheduledThreadPoolExecutor.shutdown();
                log.info("服务已关闭...");
            }
        }, "ShutDownHook"));
    }

    private void runInstances() {
        int started = 0;
        for (Instance i : instances) {
            try{
                if(i.validate()){
                    log.debug("实例{}({})启动!", i.getName(), i.getInstanceType());
                    ScheduledFuture<?> scheduledFuture = scheduledThreadPoolExecutor
                            .scheduleWithFixedDelay(i, i.getInterval(), i.getInterval(), TimeUnit.SECONDS);
                    StatefulInstance statefulInstance = new StatefulInstance(i)
                            .setTaskFuture(scheduledFuture);
                    // 成功启动的放入正在运行的实例信息中
                    runningInstance.put(i.getName(), statefulInstance);
                    started++;
                }else{
                    log.error("实例{}({})启动失败, 缺少必要参数!", i.getName(), i.getInstanceType());
                }
            }catch (Exception e){
                log.error("实例{}({})启动失败, 出现异常 => {}", i.getName(), i.getInstanceType(), e.getMessage());
            }
        }
        log.debug("成功启动 {} 个实例", started);
    }
}
