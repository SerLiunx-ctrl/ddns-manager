package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static com.serliunx.ddns.api.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * 腾讯实例定义
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@Slf4j
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
public class TencentInstance extends DefaultInstance {

    @Override
    protected void run0() {

    }
}
