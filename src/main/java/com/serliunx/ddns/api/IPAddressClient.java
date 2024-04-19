package com.serliunx.ddns.api;

import com.serliunx.ddns.api.entity.ipaddress.IPAddressResponse;
import com.serliunx.ddns.client.annotation.HttpClient;
import feign.RequestLine;

/**
 * 本机外网IP地址获取
 * @author SerLiunx
 * @since 1.0
 */
@SuppressWarnings("all")
@HttpClient(url = "http://ip-api.com")
public interface IPAddressClient {

    /**
     * 获取本机外网IP地址
     * @return IPAddressResponse
     */
    @RequestLine("GET /json")
    IPAddressResponse getIPAddress();
}
