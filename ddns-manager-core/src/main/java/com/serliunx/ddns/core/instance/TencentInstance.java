package com.serliunx.ddns.core.instance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.dnspod.v20210323.DnspodClient;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordRequest;
import com.tencentcloudapi.dnspod.v20210323.models.DescribeRecordResponse;
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

    /**
     * SecretId
     */
    private String secretId;

    /**
     * SecretKey
     */
    private String secretKey;

    /**
     * 解析记录ID
     */
    private Long recordId;

    /**
     * 记录类型，通过 API 记录类型获得，大写英文，比如：A
     */
    private String recordType;

    /**
     * 记录线路，通过 API 记录线路获得，中文，比如：默认
     */
    private String recordLine = "默认";

    @JsonIgnore
    private DnspodClient client;

    @Override
    protected void init0() {
        Credential cred = new Credential(secretId, secretKey);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setEndpoint("dnspod.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setHttpProfile(httpProfile);
        client = new DnspodClient(cred, "", clientProfile);
        debug("初始化完成.");
    }

    @Override
    protected void run0() {

    }

    @Override
    protected boolean query() {
        DescribeRecordRequest describeRecordRequest = new DescribeRecordRequest();
        describeRecordRequest.setRecordId(recordId);
        describeRecordRequest.setDomain(domain);
        try {
            DescribeRecordResponse response = client.DescribeRecord(describeRecordRequest);

        } catch (TencentCloudSDKException e) {
            error("出现异常 => {}", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean validate() {
        return false;
    }

    @SuppressWarnings("all")
    private void log(String msg, Object...params){
        log.info("[实例活动][" + name + "]" + msg, params);
    }

    @SuppressWarnings("all")
    private void debug(String msg, Object...params){
        log.debug("[实例活动][" + name + "]" + msg, params);
    }

    @SuppressWarnings("all")
    private void error(String msg, Object...params){
        log.error("[实例异常][" + name + "]" + msg, params);
    }
}
