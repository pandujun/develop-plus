package io.github.pandujun.develop.plus.web.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "request")
public class RequestConfig {

    /**
     * 代理获取接口(动态）
     */
    private String proxyGetDynamicUrl;

    public String getProxyGetDynamicUrl() {
        return proxyGetDynamicUrl;
    }

    public RequestConfig setProxyGetDynamicUrl(String proxyGetDynamicUrl) {
        this.proxyGetDynamicUrl = proxyGetDynamicUrl;
        return this;
    }
}
