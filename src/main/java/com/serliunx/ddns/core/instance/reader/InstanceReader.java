package com.serliunx.ddns.core.instance.reader;

import com.serliunx.ddns.core.instance.Instance;

import java.util.Collection;
import java.util.Set;

/**
 * 从指定途径中读取实例信息, 可以是文件、字符串、字节、流等
 * @author SerLiunx
 * @since 1.0
 */
@FunctionalInterface
public interface InstanceReader<T> {

    /**
     * 批量读取实例信息
     * @param ts 来源集合
     * @return 实例集合
     */
    Set<Instance> readAll(Collection<T> ts);
}
