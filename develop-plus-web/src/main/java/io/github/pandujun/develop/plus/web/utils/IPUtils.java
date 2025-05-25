package io.github.pandujun.develop.plus.web.utils;

import io.github.pandujun.develop.plus.core.constant.ContentTypeConstant;
import jakarta.servlet.http.HttpServletRequest;

/**
 * IP工具类
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2024/2/6 14:26
 */
public class IPUtils {
    private static final String UNKNOWN = "unknown";
    private static final String LOCAL_IP = "127.0.0.1";
    private static final String ZERO_IP = "0:0:0:0:0:0:0:1";
    /**
     * 获取IP地址
     * <p>
     * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
     * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader(ContentTypeConstant.HEADER_X_FORWARDED_FOR);
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(ContentTypeConstant.HEADER_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(ContentTypeConstant.HEADER_WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ZERO_IP.equals(ip) ? LOCAL_IP : ip;
    }

}
