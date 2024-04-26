package com.serliunx.ddns.core.instance;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.alidns20150109.AsyncClient;
import com.aliyun.sdk.service.alidns20150109.models.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.serliunx.ddns.api.constant.SystemConstants.XML_ROOT_INSTANCE_NAME;

/**
 * 阿里云实例实现
 * @author SerLiunx
 * @since 1.0
 */
@Getter
@Setter
@Slf4j
@ToString(callSuper = true)
@JacksonXmlRootElement(localName = XML_ROOT_INSTANCE_NAME)
public class AliyunInstance extends DefaultInstance {

    /**
     * AccessKey ID
     */
    private String accessKeyId;

    /**
     * AccessKey Secret
     */
    private String accessKeySecret;

    /**
     * 解析记录ID
     */
    private String recordId;

    /**
     * 主机记录。
     * 如果要解析@.example.com，主机记录要填写”@”，而不是空。
     * 示例值:
     * www
     */
    private String rr;

    /**
     * 解析记录类型
     * <li>A记录	    A	参考标准；RR值可为空，即@解析；不允许含有下划线；	IPv4地址格式
     * <li>NS记录	NS	参考标准；RR值不能为空；允许含有下划线；不支持泛解析	NameType形式
     * <li>MX记录	MX	参考标准；RR值可为空，即@解析；不允许含有下划线	NameType形式，且不可为IP地址。1-10，优先级依次递减。
     * <li>TXT记录	TXT	参考标准；另外，有效字符除字母、数字、“-”（中横杠）、还包括“_”（下划线）；RR值可为空，即@解析；允许含有下划线；
     * 不支持泛解析	字符串；长度小于512,合法字符：大小写字母，数字,空格，及以下字符：-~=:;/.@+^!*
     * <li>CNAME记录	CNAME	参考标准；另外，有效字符除字母、数字、“-”（中横杠）、还包括“_”（下划线）；RR值不允许为空（即@）；
     * 允许含有下划线	NameType形式，且不可为IP
     * <li>SRV记录	SRV	是一个name，且可含有下划线“_“和点“.”；允许含有下划线；可为空（即@）；不支持泛解析	priority：优先级，
     * 为0－65535之间的数字；weight：权重，为0－65535之间的数字；port：提供服务的端口号，为0－65535之间的数字 target：为提供服务的目标地址，
     * 为nameType，且存在。参考：<a href="https://en.wikipedia.org/wiki/SRV_record">...</a>
     * <a href="http://www.rfc-editor.org/rfc/rfc2782.txt">...</a>
     * <li>AAAA记录	AAAA	参考标准；RR值可为空，即@解析；不允许含有下划线；	IPv6地址格式
     * <li>CAA记录	CAA	参考标准；RR值可为空，即@解析；不允许含有下划线；	格式为：[flag] [tag] [value]，是由一个标志字节的[flag],
     * 和一个被称为属性的标签[tag]-值[value]对组成。例如：@ 0 issue "symantec.com"或@ 0 iodef "mailto:admin@aliyun.com"
     * <li>显性URL转发	REDIRECT_URL	参考标准；RR值可为空，即@解析	NameType或URL地址（区分大小写），长度最长为500字符，
     * 其中域名，如example.com，必须，大小写不敏感；协议：可选，如HTTP、HTTPS，默认为HTTP端口：可选，如81，默认为80；路径：可选，大小写敏感，
     * 如/path/to/，默认为/；文件名：可选，大小写敏感，如file.php，默认无；参数：可选，大小写敏感，如?user=my***，默认无。
     * <li>隐性URL转发	FORWARD_URL	参考标准；RR值可为空，即@解析	NameType或URL地址（区分大小写），长度最长为500字符，其中域名，
     * 如example.com，必须，大小写不敏感；协议：可选，如HTTP、HTTPS，默认为HTTP端口：可选，如81，默认为80；路径：可选，大小写敏感，
     * 如/path/to/，默认为/；文件名：可选，大小写敏感，如file.php，默认无；参数：可选，大小写敏感，如?user=my***，默认无。
     */
    private String type;

    /**
     * 记录值。
     * 示例值:
     * 192.0.2.254
     * <li> 无需手动指定IP，系统会自动获取。
     */
    private String value;

    @JsonIgnore
    private JsonMapper jsonMapper;

    @JsonIgnore
    private AsyncClient client;

    @Override
    protected void init0() {
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());
        client = AsyncClient.builder()
                .region("cn-hangzhou")
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride("alidns.cn-hangzhou.aliyuncs.com")
                )
                .build();
        debug("初始化完成.");
    }

    @Override
    protected void run0() {
        debug("尝试更新解析记录.");
        String publicIp = systemContext.getPublicIp();
        if(!needToUpdate(publicIp)){
            debug("当前公网已经是最新了, 无需更新.");
            return;
        }
        prevPublicIp = publicIp;
        UpdateDomainRecordRequest request = UpdateDomainRecordRequest.builder()
                .recordId(recordId)
                .rr(rr)
                .type(type)
                .value(publicIp)
                .build();
        debug("正在更新解析记录.");
        CompletableFuture<UpdateDomainRecordResponse> requestResponse = client.updateDomainRecord(request);
        try {
            requestResponse.whenComplete((v, t) -> {
                if(t != null){ //出现异常
                    handleThrowable(t);
                }else{
                    debug("操作结束, 结果: {}", v);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean query() {
        DescribeDomainRecordInfoRequest describeDomainRecordInfoRequest = DescribeDomainRecordInfoRequest.builder()
                .recordId(recordId)
                .build();
        CompletableFuture<DescribeDomainRecordInfoResponse> responseCompletableFuture =
                client.describeDomainRecordInfo(describeDomainRecordInfoRequest);
        try {
            DescribeDomainRecordInfoResponse response = responseCompletableFuture.get(5, TimeUnit.SECONDS);
            DescribeDomainRecordInfoResponseBody body = response.getBody();
            if(body != null){
                String recordValue = body.getValue();
                return !(recordValue != null && !recordValue.isEmpty()
                        && recordValue.equals(systemContext.getPublicIp()));
            }
            return false;
        } catch (InterruptedException | ExecutionException e) {
            error("出现了不应该出现的异常 => {}", e);
            return false;
        } catch (TimeoutException e) {
            error("记录查询超时! 将跳过查询直接执行更新操作.");
            return true;
        }
    }

    private void handleThrowable(Throwable t){
        error("出现异常 {}:", t.getCause(), t.getMessage());
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
