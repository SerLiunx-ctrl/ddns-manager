package com.serliunx.ddns.constant;

import com.serliunx.ddns.core.Instance;
import com.serliunx.ddns.core.instance.AliyunInstance;
import com.serliunx.ddns.core.instance.TencentInstance;
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
     * 阿里云
     */
    ALI_YUN(AliyunInstance.class),

    /**
     * 腾讯云
     */
    TENCENT_CLOUD(TencentInstance.class),
    ;

    private final Class<? extends Instance> clazz;
}
