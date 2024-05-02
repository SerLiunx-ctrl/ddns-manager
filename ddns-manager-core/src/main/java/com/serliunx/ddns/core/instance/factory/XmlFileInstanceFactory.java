package com.serliunx.ddns.core.instance.factory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceSource;

/**
 * Jackson-Xml文件实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public class XmlFileInstanceFactory extends JacksonFileInstanceFactory{

    public XmlFileInstanceFactory(String instanceDir, XmlMapper xmlMapper) {
        super(instanceDir, xmlMapper);
    }

    public XmlFileInstanceFactory(String instanceDir) {
        this(instanceDir, new XmlMapper());
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".xml"};
    }

    @Override
    protected Instance post(Instance instance) {
        instance.setInstanceSource(InstanceSource.FILE_XML);
        return instance;
    }
}
