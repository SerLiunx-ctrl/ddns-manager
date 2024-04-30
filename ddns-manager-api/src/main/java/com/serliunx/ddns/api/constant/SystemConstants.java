package com.serliunx.ddns.api.constant;

import java.io.File;

/**
 * 系统常量
 * @author SerLiunx
 * @since 1.0
 */
public final class SystemConstants {

    private SystemConstants(){throw new UnsupportedOperationException();}

    /**
     * 保存实例的文件夹
     */
    public static final String INSTANCE_FOLDER_NAME = "instances";

    /**
     * 运行目录
     */
    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * JSON文件后缀
     */
    public static final String JSON_FILE = ".json";

    /**
     * XML文件后缀
     */
    public static final String XML_FILE = ".xml";

    /**
     * YML文件后缀
     */
    public static final String YML = ".yml";

    /**
     * YAML文件后缀
     */
    public static final String YAML = ".yaml";

    /**
     * sqlite
     */
    public static final String DATABASE_SQLITE = "sqlite";

    /**
     * XML格式的实例文件根元素名称
     */
    public static final String XML_ROOT_INSTANCE_NAME = "instance";

    /**
     * 实例类型字段名
     */
    public final static String TYPE_FIELD = "instanceType";

    /**
     * 用户目录下的实例存放位置
     */
    public static final String USER_INSTANCE_DIR = USER_DIR + File.separator + INSTANCE_FOLDER_NAME;
}
