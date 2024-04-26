package com.serliunx.ddns.api.instance;

import java.util.List;

/**
 * 多数据源的实例容器, 将多种实例来源汇聚到一起
 * @see InstanceFactory
 * @see InstanceContext
 * @author SerLiunx
 * @since 1.0
 */
public interface MultipleSourceInstanceContext extends InstanceContext{

    /**
     * 添加一个实例工厂
     * @param instanceFactory 实例工厂
     */
    void addInstanceFactory(InstanceFactory instanceFactory);

    /**
     * 获取所有实例工厂
     * @return 实例工厂列表
     */
    List<InstanceFactory> getInstanceFactories();
}
