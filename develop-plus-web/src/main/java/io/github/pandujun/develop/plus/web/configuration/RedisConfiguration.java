package io.github.pandujun.develop.plus.web.configuration;

import io.github.pandujun.develop.plus.web.bean.RedisBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

/**
 * Redis配置
 * <p>
 * &#064;Author  pandujun
 * <p>
 * &#064;Date  2023/11/1 14:15
 */
@Configuration
@ConditionalOnClass(StringRedisTemplate.class)
public class RedisConfiguration {

    @Value("${spring.application.name}")
    private String projectName;

    @Bean
    @ConditionalOnMissingBean
    public RedisBean redisBean(StringRedisTemplate stringRedisTemplate) {
        stringRedisTemplate.setKeySerializer(new PrefixStringRedisSerializer(projectName));
        return new RedisBean(stringRedisTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public NoPrefixStringRedisTemplate noPrefixStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new NoPrefixStringRedisTemplate(redisConnectionFactory);
    }

    /**
     * Redis前缀封装
     * <p>
     * &#064;Author  pandujun
     * <p>
     * &#064;Date  2023/11/2 12:59
     */
    public static class PrefixStringRedisSerializer extends StringRedisSerializer {
        private final String prefix;

        public PrefixStringRedisSerializer(String prefix) {
            super(StandardCharsets.UTF_8);
            this.prefix = prefix;
        }

        @Override
        public byte[] serialize(String string) {
            return super.serialize(prefix + ":" + string);
        }
    }

    public static class NoPrefixStringRedisTemplate extends RedisTemplate<String, Object> {
        public NoPrefixStringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
            setConnectionFactory(redisConnectionFactory);
            setKeySerializer(RedisSerializer.string());
            setValueSerializer(RedisSerializer.string());
            setHashKeySerializer(RedisSerializer.string());
            setHashValueSerializer(RedisSerializer.string());
        }
    }

}
