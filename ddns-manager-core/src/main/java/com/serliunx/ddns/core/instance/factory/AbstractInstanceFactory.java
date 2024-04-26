package com.serliunx.ddns.core.instance.factory;

import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceFactory;
import com.serliunx.ddns.api.instance.InstanceType;
import com.serliunx.ddns.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.serliunx.ddns.util.InstanceUtils.validateInstance;

/**
 * 实例工厂的抽象实现
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
public abstract class AbstractInstanceFactory implements InstanceFactory{

    /**
     * 完整的实例信息
     * <li> 作为主要操作对象
     */
    private Map<String, Instance> instanceMap;

    /**
     * 实例信息缓存, 此时的实例继承关系并不完整
     * <li> 不能作为主要的操作对象
     */
    private Map<String, Instance> cacheInstanceMap;

    @Override
    public Instance getInstance(String instanceName) {
        Instance instance = instanceMap.get(instanceName);
        if(instance == null){
            throw new RuntimeException("未找到该实例信息!");
        }
        return instance;
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

    public void init(){
        Set<Instance> instances = load();
        if(instances != null && !instances.isEmpty()){
            cacheInstanceMap = new HashMap<>(instances.stream().collect(Collectors.toMap(Instance::getName, i -> i)));
            Set<Instance> builtInstances = buildInstances(instances);
            //不要直接赋值, 收集器的返回的是不可修改的Map
            instanceMap = new HashMap<>(builtInstances.stream().collect(Collectors.toMap(Instance::getName, i -> i)));
        }
    }

    /**
     * 交由子类去加载实例信息
     * @return 实例信息
     */
    protected abstract Set<Instance> load();

    /**
     * 构建完整的实例信息
     * @param instances 实例信息
     * @return 属性设置完整的实例
     */
    private Set<Instance> buildInstances(Set<Instance> instances){
        //设置实例信息, 如果需要从父类继承
        return instances.stream()
                .filter(i -> !InstanceType.INHERITED.equals(i.getInstanceType()))
                .peek(i -> {
                    String fatherInstanceName = i.getFatherInstanceName();
                    if(fatherInstanceName != null && !fatherInstanceName.isEmpty()){
                        Instance fatherInstance = cacheInstanceMap.get(fatherInstanceName);
                        if(fatherInstance != null){
                            try {
                                ReflectionUtils.copyField(fatherInstance, i, true);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                })
                .collect(Collectors.toCollection(HashSet::new));
    }
}
