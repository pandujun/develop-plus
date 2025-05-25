package io.github.pandujun.develop.plus.feign.configuration;

import feign.RequestInterceptor;
import io.github.pandujun.develop.plus.core.constant.HeaderIgnoreAutoPackageConstant;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * feign接口配置
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/11/13 17:48
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RequestInterceptor.class)
public class FeignConfiguration {

    /**
     * 给Feign设置请求头
     */
    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
//                Map<String, String> headers = this.getRequestHeaders(request);
//
//                // 传递所有请求头,防止部分丢失
//                //此处也可以只传递认证的header
//                //requestTemplate.header("Authorization", request.getHeader("Authorization"));
//                for (Map.Entry<String, String> entry : headers.entrySet()) {
//                    requestTemplate.header(entry.getKey(), entry.getValue());
//                }

                // 微服务之间传递的唯一标识,区分大小写所以通过httpServletRequest获取
                if (Objects.isNull(request.getHeader(HeaderIgnoreAutoPackageConstant.FEIGN_TAG))) {
                    String sid = String.valueOf(UUID.randomUUID());
                    requestTemplate.header(HeaderIgnoreAutoPackageConstant.FEIGN_TAG, sid);
                }
            }
        };
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            headers.put(headerName, headerValue);
        }
        return headers;
    }
}
