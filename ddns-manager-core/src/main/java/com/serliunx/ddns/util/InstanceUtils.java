package com.serliunx.ddns.util;

import com.serliunx.ddns.api.instance.Instance;

/**
 * 实例相关工具方法集合
 * @author SerLiunx
 * @since 1.0
 */
public final class InstanceUtils {
    private InstanceUtils(){throw new UnsupportedOperationException();}

    public static void validateInstance(Instance instance){
        if(instance == null){
            throw new NullPointerException();
        }
        String instanceName = instance.getName();
        if(instanceName == null || instanceName.isEmpty()){
            throw new NullPointerException();
        }
    }
}
