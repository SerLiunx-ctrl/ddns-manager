package com.serliunx.ddns.constant;

/**
 * @author SerLiunx
 * @since 1.0
 */
@SuppressWarnings("all")
public final class SystemConstants {

    private SystemConstants(){throw new UnsupportedOperationException();}

    /**
     * 保存实例的文件夹
     */
    public static final String INSTANCE_FOLDER_NAME = "instances";

    /**
     * 运行目录
     */
    public final static String USER_DIR = System.getProperty("user.dir");

    /**
     * JSON文件后缀
     */
    public final static String JSON_FILE = ".json";

    /**
     * XML文件后缀
     */
    public final static String XML_FILE = ".xml";

    /**
     * XML格式的实例文件根元素名称
     */
    public final static String XML_ROOT_INSTANCE_NAME = "instance";

    /**
     * 实例类型字段名
     */
    public final static String TYPE_FIELD = "instanceType";
}
