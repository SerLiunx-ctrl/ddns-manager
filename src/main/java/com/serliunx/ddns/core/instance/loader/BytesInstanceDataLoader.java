package com.serliunx.ddns.core.instance.loader;

import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class BytesInstanceDataLoader implements InstanceDataLoader<Byte[], InputStream>{

    @Override
    public Set<Byte[]> load(InputStream inputStream) {
        return Collections.emptySet();
    }
}
