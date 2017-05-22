package com.dushantsw.integration.cache.impl.redis;

import com.dushantsw.integration.cache.CacheStorage;
import com.dushantsw.integration.entities.Image;
import com.dushantsw.integration.entities.Images;
import com.dushantsw.integration.entities.MBID;
import com.fasterxml.jackson.core.type.TypeReference;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

public class CoverArtRedisStorage extends CacheStorage<Images> {
    public CoverArtRedisStorage(JedisPool pool) {
        super(pool);
    }

    @Override
    public Images getFromStorage(String id) throws IOException {
        return getFromStorage(id, Images.class);
    }

    @Override
    public Images getFromStorage(MBID id) throws IOException {
        return getFromStorage(id.getId());
    }
}
