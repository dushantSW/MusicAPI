package com.dushantsw.integration.cache.impl.redis;

import com.dushantsw.integration.cache.CacheStorage;
import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.entities.MBID;
import redis.clients.jedis.JedisPool;

import java.io.IOException;

public class MusicBrainzRedisStorage extends CacheStorage<Artist> {
    public MusicBrainzRedisStorage(JedisPool pool) {
        super(pool);
    }

    @Override
    public Artist getFromStorage(String id) throws IOException {
        return getFromStorage(id, Artist.class);
    }

    @Override
    public Artist getFromStorage(MBID id) throws IOException {
        return getFromStorage(id.getId());
    }

    @Override
    public boolean isStorageAvailable() {
        return this.getPool() != null && !this.getPool().isClosed();
    }
}
