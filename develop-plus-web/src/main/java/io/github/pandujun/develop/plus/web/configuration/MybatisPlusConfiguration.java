package io.github.pandujun.develop.plus.web.configuration;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.pandujun.develop.plus.core.constant.NumberConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

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
    private static final Logger logger = LoggerFactory.getLogger(MybatisPlusConfiguration.class);

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
        paginationInnerInterceptor.setMaxLimit((long) NumberConstant.HUNDRED_NUM * NumberConstant.HUNDRED_NUM);
        return paginationInnerInterceptor;
    }


    @Bean
    public IdentifierGenerator identifierGenerator() {
        long workerId = this.getWorkerId();
        long datacenterId = this.getDatacenterId();
        logger.info("mybatis ==》workerId: {}, datacenterId: {}", workerId, datacenterId);
        return new DefaultIdentifierGenerator(workerId, datacenterId);
    }

    private long getWorkerId() {
        try {
            // 优先从环境变量获取
            String workerIdStr = System.getenv("WORKER_ID");
            if (workerIdStr != null) {
                return Long.parseLong(workerIdStr);
            }

            // 从IP最后一段取模（0-31）
            String ip = InetAddress.getLocalHost().getHostAddress();
            int ipLastSegment = Integer.parseInt(ip.split("\\.")[3]);
            return ipLastSegment % 32;
        } catch (Exception e) {
            logger.warn("获取workerId失败，使用默认值1", e);
            return 1;
        }
    }

    private long getDatacenterId() {
        // 你可以用同样的方式计算dataCenterId，或者直接固定为1
        return (long) (Math.random() * 31) + 1;
    }
}
