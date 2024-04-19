package com.serliunx.ddns.api.entity.ipaddress;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * IP地址查询响应
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class IPAddressResponse {
    private String query;
    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private String lat;
    private String lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
}
