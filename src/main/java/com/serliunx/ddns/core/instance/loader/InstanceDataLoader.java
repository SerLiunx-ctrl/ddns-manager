package com.serliunx.ddns.core.instance.loader;

import java.util.Set;

/**
 * 实例数据加载器
 * <li> 用于加载实例信息
 * @author SerLiunx
 * @param <V> 实例数据类型, 如: File
 * @param <T> 具体加载器类型, 如: String(从指定路径中加载以文件形式存储的实例信息)
 * @since 1.0
 */
@FunctionalInterface
public interface InstanceDataLoader<V, T> {

    /**
     * 加载实例数据
     * @param t 具体加载器类型
     * @return 实例数据
     */
    Set<V> load(T t);
}
