package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import static com.serliunx.ddns.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
public class AliyunInstance extends AbstractInstance{

    @Override
    protected void run0() {

    }
}
