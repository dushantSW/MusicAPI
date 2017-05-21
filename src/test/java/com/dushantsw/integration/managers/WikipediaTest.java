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
    private WikipediaClient client;

    @Before
    public void beforeTests() {
        client = new DefaultWikipediaClient();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOrEmptyName() throws Throwable {
        client.getWikiPageOfArtistByName(null);
    }

    @Test(expected = AboutRetrievingException.class)
    public void testNotAvailableWikipediaPage() throws Throwable {
        String invalidName = "abba-dabba";
        client.getWikiPageOfArtistByName(invalidName);
    }

    @Test
    public void testSuccessCovertDownload() throws Throwable {
        String validName = "Bill Gates";
        About about = client.getWikiPageOfArtistByName(validName);
        assertNotNull(about);
        assertNotNull(about.getText());
        assertFalse(about.getText().isEmpty());
    }
}
