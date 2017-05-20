package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;
import com.dushantsw.integration.managers.impl.DefaultWikipediaClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * <code>WikipediaTest</code>
 *
 * @author dushantsw
 */
public class WikipediaTest {
    private CountDownLatch lock;
    private WikipediaClient client;
    private boolean status = false;

    @Before
    public void beforeTests() {
        status = false;
        lock = new CountDownLatch(1);
        client = new DefaultWikipediaClient();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOrEmptyName() throws Throwable {
        client.getWikiPageOfArtistByName(null, null);
    }

    @Test
    public void testNotAvailableWikipediaPage() throws Throwable {
        String invalidName = "abba-dabba";
        client.getWikiPageOfArtistByName(invalidName, new WikipediaCallback() {
            @Override
            public void onWikipediaInformationDownloaded(About about) {
                fail();
            }

            @Override
            public void onWikipediaInformationFailed(AboutRetrievingException ex) {
                assertNotNull(ex);
                assertEquals("Page missing", ex.getMessage());
                lock.countDown();
                status = true;
            }
        });

        lock.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(status);
    }

    @Test
    public void testSuccessCovertDownload() throws Throwable {
        String validName = "Bill Gates";
        client.getWikiPageOfArtistByName(validName, new WikipediaCallback() {
            @Override
            public void onWikipediaInformationDownloaded(About about) {
                assertNotNull(about);
                assertNotNull(about.getText());
                assertFalse(about.getText().isEmpty());
                status = true;
                lock.countDown();
            }

            @Override
            public void onWikipediaInformationFailed(AboutRetrievingException ex) {
                fail();
            }
        });

        lock.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(status);
    }
}
