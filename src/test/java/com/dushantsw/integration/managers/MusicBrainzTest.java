package com.dushantsw.integration.managers;

import com.dushantsw.integration.cache.impl.redis.MusicBrainzRedisStorage;
import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.impl.DefaultMusicBrainzClient;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * <code>MusicBrainzTest</code>
 *
 * @author dushantsw
 */
public class MusicBrainzTest {
    private MusicBrainzClient client;
    private MusicBrainzClient cacheClient;

    @Before
    public void beforeTests() {
        client = new DefaultMusicBrainzClient();

        // Cache client
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        JedisPool pool = new JedisPool(poolConfig, "localhost", 6379);
        cacheClient = new DefaultMusicBrainzClient(new MusicBrainzRedisStorage(pool));
    }

    @Test
    public void testSuccessMusicCacheBrainz() throws Throwable {
        String mbId = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";

        Artist artist = cacheClient.getArtistByMBId(mbId);
        assertNotNull(artist);
        assertEquals(mbId, artist.getMbId().getId());

        Artist artist2 = cacheClient.getArtistByMBId(mbId);
        assertNotNull(artist2);
    }
}
