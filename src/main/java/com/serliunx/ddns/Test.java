package com.serliunx.ddns;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.serliunx.ddns.constant.InstanceType;
import com.serliunx.ddns.core.instance.AliyunInstance;

/**
 * @author SerLiunx
 * @since 1.0
 */
public class Test {
    public static void main(String[] args) throws JsonProcessingException {
        AliyunInstance aliyunInstance = new AliyunInstance();
        aliyunInstance.setInstanceType(InstanceType.ALI_YUN);
        ObjectMapper xmlMapper = new XmlMapper();
        System.out.println(xmlMapper.writeValueAsString(aliyunInstance));
    }
}
