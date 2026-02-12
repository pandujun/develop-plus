package io.github.pandujun.develop.plus.web.utils;

import io.github.pandujun.develop.plus.core.result.ResultEnums;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring 工具类
 */
@Component
public class SpringUtils implements BeanFactoryPostProcessor, ApplicationContextAware {
    private static ConfigurableListableBeanFactory beanFactory;
    private static ApplicationContext applicationContext;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtils.applicationContext = applicationContext;
    }

    // 获取 Bean
    public static <T> T getBean(Class<T> clazz) {
        ListableBeanFactory listableBeanFactory = null == beanFactory ? applicationContext : beanFactory;
        if (listableBeanFactory == null) {
            throw ResultEnums.INTERNAL_SERVER_ERROR.getException("ApplicationContext 未初始化");
        }
        return listableBeanFactory.getBean(clazz);
    }
}
