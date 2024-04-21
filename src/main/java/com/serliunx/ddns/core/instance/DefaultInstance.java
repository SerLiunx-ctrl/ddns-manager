package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.serliunx.ddns.context.SystemContext;
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
     * 实例文件名
     * <li> 仅在实例信息使用文件存储时生效
     */
    protected String instanceFileName;

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
    @JsonIgnore
    protected InstanceContext instanceContext;

    /**
     * 系统参数上下文
     */
    @JsonIgnore
    protected SystemContext systemContext;

    /**
     * Spring容器
     */
    @JsonIgnore
    protected ApplicationContext applicationContext;

    /**
     * 上一次执行时获取到的最新公网IP
     * <li> 减少不必要的重复更新
     */
    protected String prevPublicIp;

    @Override
    public final void run() {
        run0();
    }

    @Override
    public final void init() {
        // 默认初始化, 设置Spring容器信息
        this.applicationContext = instanceContext.getApplicationContext();
        // 注入关键对象
        this.systemContext = applicationContext.getBean(SystemContext.class);
        // 运行子类的初始化
        init0();
    }

    @Override
    public void onClose() {}

    protected boolean needToUpdate(String publicIp){
        if(publicIp == null || publicIp.isEmpty()){
            return true;
        }
        // 公网IP有变动是才进行更新操作
        return prevPublicIp == null || !prevPublicIp.equals(publicIp);
    }

    /**
     * 实例运行逻辑, 具体实例类型逻辑需要子类实现
     */
    protected void run0(){throw new UnsupportedOperationException();}

    /**
     * 实例初始化逻辑, 具体需要子类实现
     */
    protected void init0(){};
}
