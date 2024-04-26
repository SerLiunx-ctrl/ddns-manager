package com.serliunx.ddns.core.instance.sql.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class InstanceSqlEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String instanceName;

    private String instanceType;

    private Long interval;
}
