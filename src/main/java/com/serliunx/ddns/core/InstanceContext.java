package com.serliunx.ddns.core;

import com.serliunx.ddns.core.constant.InstanceType;
import com.serliunx.ddns.core.instance.Instance;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author SerLiunx
 * @since 1.0
 */
public interface InstanceContext {

    /**
     * 获取指定类型的实例
     * @param instanceType 实例类型
     * @return 返回实例集合
     */
    Set<Instance> getInstanceOfType(InstanceType instanceType);

    /**
     * 获取所有实例信息
     * @return 所有实例信息
     */
    Set<Instance> getAllInstance();

    /**
     * 获取指定类型的实例
     * @param typeName 实例类型名称
     * @return 返回实例集合
     */
    Map<InstanceType, List<Instance>> getInstanceOfType(String typeName);
}
