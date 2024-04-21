package com.serliunx.ddns.core.instance.reader;

import com.serliunx.ddns.api.instance.InstanceType;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceReader;
import com.serliunx.ddns.util.ReflectionUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 从文件中读取实例信息, 可能为JSON格式或者XML格式.
 * @author SerLiunx
 * @since 1.0
 */
@Slf4j
@Getter
public abstract class AbstractFileInstanceReader implements InstanceReader<File> {

    /**
     * 预加载的实例信息, !!! 此时的实例信息并没有完全填充
     */
    private final Map<String, Instance> cacheInstanceMap = new HashMap<>(16);

    @Override
    public final Set<Instance> readAll(Collection<File> files) {
        //实例信息预载入
        Set<Instance> preLoadInstances = preLoad(files);

        //缓存预加载的实例信息
        preLoadInstances.forEach(i -> {
            if(!validateBasicInformation(i)){
                log.error("实例加载出现异常, 可能会丢弃部分实例信息.");
            }
            cacheInstanceMap.put(i.getName(), i);
        });
        return buildInstances(preLoadInstances);
    }

    /**
     * 预载入实例信息
     * @param files 实例文件信息
     * @return 实例信息(只有通用属性, 如名称、类型、父类名称)
     */
    protected abstract Set<Instance> preLoad(Collection<File> files);

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

    @SuppressWarnings("all")
    private boolean validateBasicInformation(Instance instance){
        if(instance.getName() == null || instance.getName().isEmpty() ||
                instance.getInstanceType() == null){
            return false;
        }
        return true;
    }
}
