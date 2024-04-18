package com.serliunx.ddns;

import com.serliunx.ddns.core.DefaultInstanceContext;
import com.serliunx.ddns.core.InstanceContext;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class Test {
    public static void main(String[] args) throws Exception {
        InstanceContext instanceContext = new DefaultInstanceContext();
        instanceContext.getAllInstance()
                .forEach(System.out::println);
    }
}
