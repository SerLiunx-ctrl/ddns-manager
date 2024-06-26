package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceSource;
import com.serliunx.ddns.api.instance.InstanceType;
import com.serliunx.ddns.api.instance.context.InstanceContext;
import com.serliunx.ddns.context.SystemContext;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationContext;

import static com.serliunx.ddns.api.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * 实例的默认实现, 定义公共逻辑
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
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
     * 域名
     */
    protected String domain;

    /**
     * 记录值。
     * 示例值:
     * 192.0.2.254
     * <li> 无需手动指定IP，系统会自动获取。
     */
    protected String value;

    /**
     * 实例来源
     */
    protected InstanceSource instanceSource;

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
        // 在执行前查询记录的信息, 同样则不更新记录信息
        if(query()){
            run0();
        }
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

    /**
     * 实例参数校验, 每种实例类型参数要求不同. 交由子类实现
     * <li> 实例名称和类型已由实例工厂校验、子类无需重复校验
     * @return 校验通过返回真, 否则返回假. 默认返回真, 代表无需校验参数
     */
    public boolean validate(){return true;}

    @Override
    public InstanceSource getInstanceSource() {
        return instanceSource;
    }

    @Override
    public void setInstanceSource(InstanceSource instanceSource) {
        this.instanceSource = instanceSource;
    }

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
     * 运行前查询
     * <li> 返回真则需要更新此实例所对应的解析记录
     */
    protected boolean query(){return true;}

    /**
     * 实例初始化逻辑, 具体需要子类实现
     */
    protected void init0(){};
}
