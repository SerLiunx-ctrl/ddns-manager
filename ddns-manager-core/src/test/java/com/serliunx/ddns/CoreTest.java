package com.serliunx.ddns;

import com.serliunx.ddns.api.constant.SystemConstants;
import com.serliunx.ddns.api.instance.InstanceFactory;
import com.serliunx.ddns.api.instance.MultipleSourceInstanceContext;
import com.serliunx.ddns.core.DefaultInstanceContext;
import com.serliunx.ddns.core.instance.factory.JsonFileInstanceFactory;
import org.junit.Test;

/**
 * 测试
 * @author SerLiunx
 * @since 1.0
 */
public class CoreTest {

    @Test
    public void factoryTest(){
        InstanceFactory factory = new JsonFileInstanceFactory(SystemConstants.USER_INSTANCE_DIR);
        MultipleSourceInstanceContext instanceContext = new DefaultInstanceContext();
        instanceContext.addInstanceFactory(factory);
        instanceContext.refresh();
        instanceContext.getInstances().forEach(System.out::println);
    }
}
