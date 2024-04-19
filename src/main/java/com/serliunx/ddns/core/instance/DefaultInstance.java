package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.serliunx.ddns.core.InstanceContext;
import com.serliunx.ddns.core.constant.InstanceType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationContext;

/**
 * 实例的默认实现, 定义公共逻辑
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultInstance implements Instance {

    /**
     * 实例类型
     */
    protected InstanceType instanceType;

    /**
     * 执行周期(秒)
     */
    protected Long interval;

    /**
     * 实例名称
     * <li> 全局唯一
     */
    protected String name;

    /**
     * 父类实例名称
     */
    protected String fatherInstanceName;

    /**
     * 实例上下文
     */
    protected transient InstanceContext instanceContext;

    /**
     * Spring容器
     */
    protected transient ApplicationContext applicationContext;

    @Override
    public final void run() {
        run0();
    }

    @Override
    public final void init() {
        // 默认初始化, 设置Spring容器信息
        this.applicationContext = instanceContext.getApplicationContext();
        // 运行子类的初始化
        init0();
    }

    @Override
    public void onClose() {}

    /**
     * 实例运行逻辑, 具体实例类型逻辑需要子类实现
     */
    protected void run0(){throw new UnsupportedOperationException();}

    /**
     * 实例初始化逻辑, 具体需要子类实现
     */
    protected void init0(){};
}
