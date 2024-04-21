package com.serliunx.ddns.api.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 实例类型: 阿里云、华为云、腾讯云等
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum InstanceType {

    /**
     * 可继承的实例
     * <li> 比较该类型为可继承的实例
     * <li> 用于实例的某些参数可复用的情况
     */
    INHERITED,

    /**
     * 阿里云
     */
    ALI_YUN,

    /**
     * 腾讯云
     */
    TENCENT_CLOUD,
}
