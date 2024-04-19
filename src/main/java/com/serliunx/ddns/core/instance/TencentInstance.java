package com.serliunx.ddns.core.instance;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@Slf4j
@ToString(callSuper = true)
public class TencentInstance extends DefaultInstance {

    @Override
    protected void run0() {
        log.info("腾讯云实例运行中...");
    }
}
