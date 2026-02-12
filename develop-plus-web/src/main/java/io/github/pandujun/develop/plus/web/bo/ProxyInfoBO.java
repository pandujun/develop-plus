package io.github.pandujun.develop.plus.web.bo;

import java.time.LocalDateTime;

public class ProxyInfoBO {
    /**
     * ip
     */
    private String ip;
    /**
     * 端口
     */
    private String port;
    /**
     * 省份
     */
    private String prov;
    /**
     * 城市
     */
    private String city;
    /**
     * 过期时间
     */
    private LocalDateTime expire;

    public String getIp() {
        return ip;
    }

    public ProxyInfoBO setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getPort() {
        return port;
    }

    public ProxyInfoBO setPort(String port) {
        this.port = port;
        return this;
    }

    public String getProv() {
        return prov;
    }

    public ProxyInfoBO setProv(String prov) {
        this.prov = prov;
        return this;
    }

    public String getCity() {
        return city;
    }

    public ProxyInfoBO setCity(String city) {
        this.city = city;
        return this;
    }

    public LocalDateTime getExpire() {
        return expire;
    }

    public ProxyInfoBO setExpire(LocalDateTime expire) {
        this.expire = expire;
        return this;
    }
}
