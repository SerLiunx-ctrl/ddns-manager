package com.serliunx.ddns.core.instance.reader;

import com.serliunx.ddns.core.Instance;

import java.io.File;

/**
 * 实例加载器
 * @author SerLiunx
 * @since 1.0
 */
public interface InstanceReader {

    /**
     * 从文件中读取实例
     * @param file 文件
     * @return 实例
     */
    Instance read(File file);
}
