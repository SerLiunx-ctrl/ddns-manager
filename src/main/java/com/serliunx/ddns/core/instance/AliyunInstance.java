package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.serliunx.ddns.api.IPAddressClient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static com.serliunx.ddns.core.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@Slf4j
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
public class AliyunInstance extends DefaultInstance {

    private String privateKey;
    private String uuid;
    private IPAddressClient ipAddressClient;

    @Override
    protected void init0() {
        ipAddressClient = applicationContext.getBean(IPAddressClient.class);
    }

    @Override
    protected void run0() {
        log.info("本机ip => {}", ipAddressClient.getIPAddress().getQuery());
    }
}
