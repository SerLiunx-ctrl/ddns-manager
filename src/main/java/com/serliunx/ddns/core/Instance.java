package com.serliunx.ddns.core;

import com.serliunx.ddns.constant.InstanceType;

/**
 * 实例对象
 * @author SerLiunx
 * @since 1.0
 */
public interface Instance extends Runnable {

    /**
     * 获取实例名称
     * @return 实例名称
     */
    String getName();

    /**
     * 获取实例类型
     * @return 实例类型
     */
    InstanceType getInstanceType();
}
