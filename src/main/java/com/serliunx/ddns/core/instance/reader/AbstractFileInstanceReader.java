package com.serliunx.ddns.core.instance.reader;

import com.serliunx.ddns.core.constant.InstanceType;
import com.serliunx.ddns.core.instance.Instance;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Field;
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
    protected Set<Instance> buildInstances(Set<Instance> instances){
        //筛选出用于继承的实例(用于继承的实例没有实际作用, 仅仅用于继承公用参数)
        Map<String, Instance> inheritedInstances = instances.stream()
                .filter(i -> i.getInstanceType().equals(InstanceType.INHERITED))
                .collect(Collectors.toMap(Instance::getName, i -> i));
        //设置实例信息, 如果需要从父类继承
        HashSet<Instance> filteredInstances = instances.stream()
                .filter(i -> i.getFatherInstanceName() != null && !i.getFatherInstanceName().isEmpty())
                .collect(Collectors.toCollection(HashSet::new));
        filteredInstances.forEach(i -> {
            Instance fatherInstance = inheritedInstances.get(i.getFatherInstanceName());
            if(fatherInstance != null){
                try {
                    copyFields(fatherInstance, i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return filteredInstances;
    }

    private void copyFields(Instance parent, Instance child) throws Exception{
        // 获取parent对象的Class对象
        Class<?> parentClass = parent.getClass();
        // 获取child对象的Class对象
        Class<?> childClass = child.getClass();

        // 遍历child对象的所有属性，包括父类的属性
        while (childClass != null) {
            for (Field parentField : parentClass.getDeclaredFields()) {
                // 获取属性名称
                String fieldName = parentField.getName();
                // 根据属性名称获取child对象中对应的属性
                Field childField;
                try {
                    childField = childClass.getDeclaredField(fieldName);
                } catch (NoSuchFieldException e) {
                    // 如果child对象中没有同名属性，则继续遍历下一个属性
                    continue;
                }
                // 设置属性可访问
                parentField.setAccessible(true);
                childField.setAccessible(true);
                // 如果child对象中的属性值为null，则复制parent对象中的属性值
                if (childField.get(child) == null) {
                    Object value = parentField.get(parent);
                    childField.set(child, value);
                }
            }
            // 获取父类的Class对象，以便继续遍历父类的属性
            childClass = childClass.getSuperclass();
        }
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
