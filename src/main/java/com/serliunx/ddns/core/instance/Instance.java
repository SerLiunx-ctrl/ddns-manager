package com.serliunx.ddns.core.instance;

import com.serliunx.ddns.core.InstanceContext;
import com.serliunx.ddns.core.constant.InstanceType;

/**
 * 实例对象
 * @author SerLiunx
 * @since 1.0
 */
public interface Instance extends Runnable {

    /**
     * 初始化实例
     */
    void init();

    /**
     * 线程池准备关闭时的钩子函数
     */
    void onClose();

    /**
     * 获取实例名称
     * @return 实例名称
     */
    String getName();

    /**
     * 获取父实例名称
     * @return 父实例名称
     */
    String getFatherInstanceName();

    /**
     * 获取实例类型
     * @return 实例类型
     */
    InstanceType getInstanceType();

    /**
     * 获取实例的执行周期
     * @return 执行周期
     */
    Long getInterval();

    /**
     * 设置实例上下文
     * @param instanceContext 实例上下文
     */
    void setInstanceContext(InstanceContext instanceContext);
}
