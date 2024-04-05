package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.Instance;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 实例的抽象实现, 定义公共逻辑
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractInstance implements Instance {

    /**
     * 实例类型
     */
    protected InstanceType instanceType;

    /**
     * 实例名称
     * <li> 全局唯一
     */
    protected String name;

    @Override
    public void run() {
        run0();
    }

    /**
     * 实例运行逻辑, 具体实例类型逻辑需要子类实现
     */
    protected void run0(){throw new UnsupportedOperationException();}
}
