package com.serliunx.ddns.core.instance.sql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.serliunx.ddns.core.instance.sql.InstanceDataTypeHandler;
import com.serliunx.ddns.core.instance.sql.support.annotation.ExpandParamField;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 实例sql实体
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@ToString
@TableName("ddns_instances")
@Deprecated
public class InstanceSqlEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 实例名称(全局唯一)
     */
    private String instanceName;

    /**
     * 实例类型{@link com.serliunx.ddns.api.instance.InstanceType}
     */
    private String instanceType;

    /**
     * 执行周期(单位秒)
     */
    private Long interval;

    /**
     * 实例数据,一般为或JSON格式数据(仅在使用数据库存储实例信息时生效)
     */
    @TableField(typeHandler = InstanceDataTypeHandler.class)
    @ExpandParamField
    private String instanceData;
}
