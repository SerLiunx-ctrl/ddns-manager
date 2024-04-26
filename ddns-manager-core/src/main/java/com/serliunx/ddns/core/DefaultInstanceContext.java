package com.serliunx.ddns.core;

import com.serliunx.ddns.api.instance.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.serliunx.ddns.util.InstanceUtils.validateInstance;

/**
 * 实例容器的默认实现、多数据源的实例容器
 * @see MultipleSourceInstanceContext
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Slf4j
@Component
public class DefaultInstanceContext implements MultipleSourceInstanceContext, InstanceContext, ApplicationContextAware {

    private ApplicationContext applicationContext;
    private final List<InstanceFactory> instanceFactories;

    private Map<String, Instance> instanceMap;

    public DefaultInstanceContext() {
        instanceFactories = new ArrayList<>(16);
        instanceMap = new HashMap<>(16);
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public boolean addInstance(Instance instance, boolean override) {
        validateInstance(instance);
        Instance i = instanceMap.get(instance.getName());
        if(override && i != null){
            return false;
        }
        instanceMap.put(instance.getName(), instance);
        return true;
    }

    @Override
    public void addInstance(Instance instance) {
        addInstance(instance, false);
    }

    @Override
    public Instance getInstance(String instanceName) {
        Instance instance = instanceMap.get(instanceName);
        if(instance == null){
            throw new RuntimeException("未找到该实例信息!");
        }
        return instance;
    }

    @Override
    public Map<String, Instance> getInstanceOfType(InstanceType type) {
        return instanceMap.values()
                .stream()
                .filter(i -> i.getInstanceType().equals(type))
                .collect(Collectors.toMap(Instance::getName, i -> i));
    }

    @Override
    public Set<Instance> getInstances() {
        return new HashSet<>(instanceMap.values());
    }

    @Override
    public void addInstanceFactory(InstanceFactory instanceFactory) {
        this.instanceFactories.add(instanceFactory);
    }

    @Override
    public List<InstanceFactory> getInstanceFactories() {
        return instanceFactories;
    }

    @Override
    public void refresh() {
        if(!instanceFactories.isEmpty()){
            // 初始化所有实例工厂
            instanceFactories.forEach(InstanceFactory::init);
            // 加载、过滤所有实例
            Set<Instance> instances = new HashSet<>();
            instanceFactories.forEach(f -> instances.addAll(f.getInstances()));
            // 不要直接赋值, 收集器的返回的是不可修改的Map
            instanceMap = new HashMap<>(instances.stream().collect(Collectors.toMap(Instance::getName, i -> i)));
        }
    }

    @Override
    @SuppressWarnings("all")
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
