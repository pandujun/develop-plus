package com.pandj.develop.plus.web.configuration;

import com.pandj.develop.plus.web.advice.AutoPackagingResponseBodyAdvice;
import com.pandj.develop.plus.web.advice.GlobalServerExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 通用配置
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/11/1 14:26
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(ResponseBodyAdvice.class)
public class CommonConfiguration {

    /**
     * 自动封装
     */
    @Bean
    @ConditionalOnMissingBean
    public AutoPackagingResponseBodyAdvice autoPackagingResponseBodyAdvice() {
        return new AutoPackagingResponseBodyAdvice();
    }

    /**
     * 全局异常封装
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalServerExceptionHandler globalServerExceptionHandler() {
        return new GlobalServerExceptionHandler();
    }
}
