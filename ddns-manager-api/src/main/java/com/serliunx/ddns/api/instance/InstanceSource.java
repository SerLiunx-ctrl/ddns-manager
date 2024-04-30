package com.serliunx.ddns.api.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.serliunx.ddns.api.constant.SystemConstants.*;

/**
 * 实例来源
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum InstanceSource {
    FILE_JSON(JSON_FILE),
    FILE_XML(XML_FILE),
    FILE_YML(YML),
    DATABASE(DATABASE_SQLITE),

    UNKNOWN("未知"),
    ;

    /**
     * 来源标签
     * <li> 如果是从文件加载的实例信息, 标签即表示文件后缀名
     * <li> 如果是来自数据库, 标签即表示数据库类型
     */
    private final String sourceTag;
}
