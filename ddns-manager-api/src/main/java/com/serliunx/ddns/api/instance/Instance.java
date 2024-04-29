package com.serliunx.ddns.api.instance;

import com.serliunx.ddns.api.instance.context.InstanceContext;

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
     * 实例参数校验
     * <li> 实例名称和类型已由实例工厂校验、子类无需重复校验
     * @return 校验通过返回真, 否则返回假. 默认返回真, 代表无需校验参数
     */
    boolean validate();

    /**
     * 设置实例上下文
     * @param instanceContext 实例上下文
     */
    void setInstanceContext(InstanceContext instanceContext);
}
