package com.serliunx.ddns.core.instance.factory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.serliunx.ddns.api.constant.SystemConstants;
import com.serliunx.ddns.api.instance.Instance;
import com.serliunx.ddns.api.instance.InstanceType;

import java.io.File;

import static com.serliunx.ddns.core.InstanceTypes.match;

/**
 * Xml文件实例工厂
 * @author SerLiunx
 * @since 1.0
 */
public class XmlFileInstanceFactory extends FileInstanceFactory{

    private final XmlMapper xmlMapper;

    public XmlFileInstanceFactory(String instanceDir, XmlMapper xmlMapper) {
        super(instanceDir);
        this.xmlMapper = xmlMapper;
    }

    public XmlFileInstanceFactory(String instanceDir) {
        this(instanceDir, new XmlMapper());
    }

    @Override
    protected Instance loadInstance(File file) {
        return jacksonFileLoad(xmlMapper, file);
    }

    @Override
    protected String[] fileSuffix() {
        return new String[]{".xml"};
    }
}
