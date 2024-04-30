package com.serliunx.ddns.core;

import com.serliunx.ddns.api.instance.Instance;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.ScheduledFuture;

/**
 * 实例包装, 用于存储实例状态信息: 运行中或已停止
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Accessors(chain = true)
public final class StatefulInstance {

    @Setter
    private ScheduledFuture<?> taskFuture;
    private final Instance instance;

    public StatefulInstance(Instance instance) {
        this.instance = instance;
    }
}
