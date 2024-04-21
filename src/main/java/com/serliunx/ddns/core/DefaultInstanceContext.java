package com.serliunx.ddns.core;

import com.serliunx.ddns.core.constant.InstanceType;
import com.serliunx.ddns.core.constant.SystemConstants;
import com.serliunx.ddns.core.instance.Instance;
import com.serliunx.ddns.core.instance.loader.FileInstanceDataLoader;
import com.serliunx.ddns.core.instance.reader.AbstractFileInstanceReader;
import com.serliunx.ddns.core.instance.reader.JsonFileInstanceReader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Slf4j
@Component
public class DefaultInstanceContext implements InstanceContext, ApplicationContextAware {

    private final FileInstanceDataLoader instanceDataLoader;
    private final AbstractFileInstanceReader instanceReader;
    private final String instanceDir;

    private Set<Instance> instances;
    private ApplicationContext applicationContext;

    private volatile boolean initialized = false;

    private final Object refreshLock = new Object();

    public DefaultInstanceContext(FileInstanceDataLoader instanceDataLoader, AbstractFileInstanceReader instanceReader,
                                  String instanceDir) {
        this.instanceDataLoader = instanceDataLoader;
        this.instanceReader = instanceReader;
        this.instances = new HashSet<>();
        this.instanceDir = instanceDir;
        refresh();
    }

    public DefaultInstanceContext() {
        this(new FileInstanceDataLoader(), new JsonFileInstanceReader(), SystemConstants.USER_INSTANCE_DIR);
    }

    @Override
    public Set<Instance> getInstanceOfType(InstanceType instanceType) {
        if(instanceType == null){
            throw new NullPointerException();
        }
        if(!initialized){
            throw new RuntimeException("还在初始化中! 请稍候");
        }
        return instances.stream()
                .filter(instance -> instance.getInstanceType().equals(instanceType))
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public Set<Instance> getAllInstance() {
        if(!initialized){
            throw new RuntimeException("还在初始化中! 请稍候");
        }
        return instances;
    }

    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void refresh(){
        if(initialized){
            return;
        }
        synchronized (refreshLock){
            Set<File> load = instanceDataLoader.load(instanceDir);
            this.instances = instanceReader.readAll(load);
            this.initialized = true;
        }
    }
}
