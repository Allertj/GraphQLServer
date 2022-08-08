package com.ajvm.graphqlserver

import com.ajvm.graphqlserver.db.ProductInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.github.cdimascio.dotenv.dotenv
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.io.Serializable

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration::class)
@EnableCaching
class RedisConfiguration {
//    val dotenv = dotenv {        directory = "./" }
//    val redisHost: String = dotenv["spring.redis.host"]
//    private val redisPort = dotenv["spring.redis.port"]

    @Bean
    fun redisCacheTemplate(redisConnectionFactory: LettuceConnectionFactory?): RedisTemplate<String, Serializable> {
        val template = RedisTemplate<String, Serializable>()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer()
        template.setConnectionFactory(redisConnectionFactory!!)
        return template
    }

    @Bean
    fun cacheManager(factory: RedisConnectionFactory?): CacheManager {
        val objectMapper =
            ObjectMapper()
                .registerModule(KotlinModule())
                .registerModule(JavaTimeModule())
                .enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)

        val serializer = GenericJackson2JsonRedisSerializer(objectMapper)
        val config = RedisCacheConfiguration.defaultCacheConfig()
        val redisCacheConfiguration = config
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair
                    .fromSerializer(GenericJackson2JsonRedisSerializer())
            )
        return RedisCacheManager.builder(factory!!).cacheDefaults(redisCacheConfiguration)
            .build()
    }
}