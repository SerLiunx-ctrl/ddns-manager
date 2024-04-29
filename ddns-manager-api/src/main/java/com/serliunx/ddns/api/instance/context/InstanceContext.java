package com.serliunx.ddns.api.instance.context;

import com.serliunx.ddns.api.instance.InstanceFactory;
import org.springframework.context.ApplicationContext;

/**
 * 实例容器
 * @author SerLiunx
 * @since 1.0
 */
public interface InstanceContext extends InstanceFactory {

    /**
     * 获取Spring容器
     * @return Spring容器
     */
    ApplicationContext getApplicationContext();

    /**
     * 刷新实例容器
     */
    void refresh();

    /**
     * 对于实例容器来说, 初始化工厂就是刷新实例容器
     */
    @Override
    default void init() {
        refresh();
    }
}
