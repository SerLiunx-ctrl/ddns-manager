package com.serliunx.ddns.core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * 实例载入信息
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public class InstanceLoadContext {

    /**
     * 读取到的实例数量
     */
    private final int instanceCount;

    /**
     * 实例集合
     */
    private final Set<Instance> instances;
}
