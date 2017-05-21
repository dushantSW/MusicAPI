package com.dushantsw.integration.cache;

import com.dushantsw.integration.entities.MBID;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * <code>CacheStorage</code>
 *
 * @author dushantsw
 */
@SuppressWarnings("WeakerAccess")
@Data
public abstract class CacheStorage<T> {
    public final ObjectMapper mapper;
    private final JedisPool pool;

    public CacheStorage(JedisPool pool) {
        this.pool = pool;
        this.mapper = new ObjectMapper();
    }

    public T getFromStorage(String id, Class<T> cls) throws IOException {
        Jedis jedis = getPool().getResource();
        String redisString = jedis.get(id);
        if (redisString == null || redisString.isEmpty()) return null;
        System.out.println("Redis read for (id): " + id);
        jedis.close();

        String jsonString = new String(Base64.getDecoder().decode(redisString), "utf-8");
        return mapper.readValue(jsonString, cls);
    }

    public abstract T getFromStorage(String id) throws IOException;
    public abstract T getFromStorage(MBID id) throws IOException;

    public void storeIntoStorage(String id, T object) throws JsonProcessingException, UnsupportedEncodingException {
        String asString = mapper.writeValueAsString(object);
        if (asString == null || asString.isEmpty()) return;

        String redisString = Base64.getEncoder().encodeToString(asString.getBytes("utf-8"));
        if (StringUtils.isEmpty(redisString)) return;
        Jedis resource = pool.getResource();
        resource.set(id, redisString);
        resource.close();

        System.out.println("Redis stored for (id): " + id);
    }

    public void storeIntoStorage(MBID id, T object) throws JsonProcessingException, UnsupportedEncodingException {
        storeIntoStorage(id.getId(), object);
    }

    public boolean isStorageAvailable() {
        return getPool() != null && !getPool().isClosed();
    }
}
