package com.serliunx.ddns.core.instance.reader;

import com.serliunx.ddns.core.Instance;

import java.io.File;

/**
 * 实例加载器 - 抽象实现, 实现公共逻辑
 * @author SerLiunx
 * @since 1.0
 */
public abstract class AbstractInstanceReader implements InstanceReader {

    @Override
    public Instance read(File file) {
        return null;
    }
}
