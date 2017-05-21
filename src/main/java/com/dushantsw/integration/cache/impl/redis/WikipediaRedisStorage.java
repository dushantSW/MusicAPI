package com.dushantsw.integration.cache.impl.redis;

import com.dushantsw.integration.cache.CacheStorage;
import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.entities.MBID;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

/**
 * <code>WikipediaRedisStorage</code>
 *
 * @author dushantsw
 */
public class WikipediaRedisStorage extends CacheStorage<About> {
    public WikipediaRedisStorage(JedisPool pool) {
        super(pool);
    }

    @Override
    public About getFromStorage(String id) throws IOException {
        return getFromStorage(id, About.class);
    }

    @Override
    public About getFromStorage(MBID id) throws IOException {
        return null;
    }
}
