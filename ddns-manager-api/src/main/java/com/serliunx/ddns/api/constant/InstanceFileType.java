package com.serliunx.ddns.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 保存实例的文件类型: XML、JSON等
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum InstanceFileType {
    XML(".xml"),
    JSON(".json"),
    YML(".yml"),
    YAML(".yaml"),
    ;
    private final String value;
}
