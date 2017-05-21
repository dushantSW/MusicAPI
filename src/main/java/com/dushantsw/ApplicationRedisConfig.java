package com.dushantsw;

import com.dushantsw.integration.managers.RedisManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Configuration
@PropertySource("classpath:redis-config.properties")
public class ApplicationRedisConfig {

    @Value("${redis.url}")
    private String redisUrl;

    @Value("${redis.port}")
    private int redisPort;

    @PostConstruct
    public void initRedisConfig() {
        RedisManager redisManager = RedisManager.getSharedManager();
        redisManager.initConnection(redisUrl, redisPort);
    }
}
