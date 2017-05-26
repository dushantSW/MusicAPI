package com.dushantsw.integration.services;

import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.exceptions.MusicBrainzException;
import com.dushantsw.services.MusicAPIService;
import com.mashape.unirest.http.Unirest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClients;
import org.apache.http.impl.client.cache.ExponentialBackOffSchedulingStrategy;
import org.apache.http.protocol.HttpContext;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.Assert.assertNotNull;

/**
 * <code>MusicAPIServiceTest</code>
 *
 * @author dushantsw
 */
public class MusicAPIServiceTest {
    private MusicAPIService service;

    @Before
    public void beforeTest() {
        // Add caching options before tests start.
        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(1000)
                .setMaxObjectSize(8192)
                .setSharedCache(false)
                .build();
        CloseableHttpClient cachingClient = CachingHttpClients.custom()
                .setCacheConfig(cacheConfig)
                .setSchedulingStrategy(new ExponentialBackOffSchedulingStrategy(cacheConfig))
                .setRetryHandler(new DefaultHttpRequestRetryHandler(5, false))
                .setServiceUnavailableRetryStrategy(strategy)
                .build();
        Unirest.setHttpClient(cachingClient);
        this.service = new MusicAPIService();
        this.service.initApiService();
    }

    @Test
    public void testMeasure() throws Throwable {
        Instant start = Instant.now();
        String mbId = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        Artist artist = service.getArtistByMBId(mbId);
        assertNotNull(artist);
        Instant end = Instant.now();
        System.out.println(Duration.between(start, end));
    }

    private final ServiceUnavailableRetryStrategy strategy = new ServiceUnavailableRetryStrategy() {
        @Override
        public boolean retryRequest(HttpResponse response, int executionCount, HttpContext context) {
            int statusCode = response.getStatusLine().getStatusCode();
            return statusCode >= 500 && executionCount < 6;
        }

        @Override
        public long getRetryInterval() {
            return 0;
        }
    };
}
