package com.dushantsw.integration.managers;

import lombok.Getter;
import org.apache.commons.pool2.impl.GenericObjectPool;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <code>RedisManager</code>
 *
 * @author dushantsw
 */
public class RedisManager {
    private static RedisManager redisManager;
    private RedisManager() {}

    @Getter
    private JedisPool jedisPool;

    public static RedisManager getSharedManager() {
        if (redisManager == null) redisManager = new RedisManager();
        return redisManager;
    }

    public void initConnection(String redisUrl, int redisPort) {
        if (redisUrl == null || redisPort <= 0)
            return;

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        jedisPool = new JedisPool(poolConfig, redisUrl, redisPort);
    }
}
