package com.pandj.develop.plus.web.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus配置
 * </P>
 * &#064;Auth0r pandujun
 * </P>
 * &#064;Date 2024/11/28
 */
@Configuration
@ConditionalOnClass(MybatisPlusInterceptor.class)
public class MybatisPlusConfiguration {
    /**
     * 目前，MyBatis-Plus 提供了以下插件：
     * <p>
     * 自动分页: PaginationInnerInterceptor
     * 多租户: TenantLineInnerInterceptor
     * 动态表名: DynamicTableNameInnerInterceptor
     * 乐观锁: OptimisticLockerInnerInterceptor
     * SQL 性能规范: IllegalSQLInnerInterceptor
     * 防止全表更新与删除: BlockAttackInnerInterceptor
     * <p>
     * <p>
     * 建议的顺序是：
     * <p>
     * 多租户、动态表名
     * 分页、乐观锁
     * SQL 性能规范、防止全表更新与删除
     * 总结：对 SQL 进行单次改造的插件应优先放入，不对 SQL 进行改造的插件最后放入。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件
        interceptor.addInnerInterceptor(this.getPaginationInnerInterceptor());
        // 添加非法SQL拦截器
        interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        //添加防止全表更新与删除插件
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * 分页插件
     */
    private PaginationInnerInterceptor getPaginationInnerInterceptor() {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MARIADB);
        paginationInnerInterceptor.setMaxLimit(10000L);
        return paginationInnerInterceptor;
    }
}
