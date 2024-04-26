package com.serliunx.ddns.api.instance;

import java.util.Map;
import java.util.Set;

/**
 * 实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public interface InstanceFactory {

    /**
     * 添加实例
     * <li> 此方法默认为不覆盖的方式添加, 即如果存在则添加失败, 没有任何返回值和异常.
     * @param instance 实例信息
     */
    void addInstance(Instance instance);

    /**
     * 初始化实例工厂
     * <li> 创建工厂之后务必手动调用, 否则不会加载、构建任何实例信息
     */
    void init();

    /**
     * 添加实例
     * @param instance 实例信息
     * @param override 是否覆盖原有的同名实例
     * @return 成功添加返回真, 否则返回假
     */
    boolean addInstance(Instance instance, boolean override);

    /**
     * 根据实例名称获取实例
     * @param instanceName 实例名称
     * @return 实例信息, 如果不存在则会抛出异常
     */
    Instance getInstance(String instanceName);

    /**
     * 获取指定类型的实例
     * @param type 类型
     * @return 实例名称-实例信息 键值对.
     */
    Map<String, Instance> getInstanceOfType(InstanceType type);

    /**
     * 获取所有已加载的实例信息
     * @return 所有实例信息
     */
    Set<Instance> getInstances();

    /**
     * 获取指定类型的实例
     * @param type 类型名称
     * @return 实例名称-实例信息 键值对.
     */
    default Map<String, Instance> getInstanceOfType(String type) {
        return getInstanceOfType(InstanceType.valueOf(type));
    }
}
