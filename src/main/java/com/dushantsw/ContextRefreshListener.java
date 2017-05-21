package com.dushantsw;


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
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * <code>ContextRefreshListener</code> implements {@link ApplicationListener} is called
 * for setting up http client cache and retry options.
 *
 * @author dushantsw
 */
@Component
public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(1000)
                .setMaxObjectSize(8192)
                .setSharedCache(false)
                .build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .build();
        CloseableHttpClient cachingClient = CachingHttpClients.custom()
                .setCacheConfig(cacheConfig)
                .setSchedulingStrategy(new ExponentialBackOffSchedulingStrategy(cacheConfig))
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(5, false))
                .setServiceUnavailableRetryStrategy(strategy)
                .build();
        Unirest.setHttpClient(cachingClient);
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
