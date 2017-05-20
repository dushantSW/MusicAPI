package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.Images;
import com.dushantsw.integration.managers.exceptions.ImageRetrievingException;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.impl.DefaultCoverArtArchiveClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

/**
 * <code>CoverArtTest</code>
 *
 * @author dushantsw
 */
public class CoverArtTest {
    private CountDownLatch lock;
    private CoverArtArchiveClient client;
    private boolean status = false;

    @Before
    public void beforeTests() {
        status = false;
        lock = new CountDownLatch(1);
        client = new DefaultCoverArtArchiveClient();
    }

    @Test(expected = InvalidMBIDException.class)
    public void testNullOrEmptyMbId() throws Throwable {
        client.getCoverImagesByMBID(null, null);
    }

    @Test(expected = InvalidMBIDException.class)
    public void testInvalidUUIDMBID() throws Throwable {
        client.getCoverImagesByMBID("asdas-asdasgsgsdgsdgsdgsdgsgsdgs", null);
    }

    @Test
    public void testNotAvailableCoverArt() throws Throwable {
        // Checked uuid, it should throw 404 and get caught by client.
        String INVALID_MBID = "1b023e01-4d56-387b-8758-8678046e4cef";
        client.getCoverImagesByMBID(INVALID_MBID, new CoverArtCallback() {
            @Override
            public void onCovertArtRetrievingFinished(Images images) {
                fail();
            }

            @Override
            public void onCovertArtRetrievingFailed(ImageRetrievingException ex) {
                Assert.assertNotNull(ex);
                Assert.assertEquals("cover_art_not_found", ex.getMessage());
                lock.countDown();
                status = true;
            }
        });

        lock.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(status);
    }

    @Test
    public void testSuccessCovertDownload() throws Throwable {
        String RELEASE_MBID = "1b022e01-4da6-387b-8658-8678046e4cef";
        client.getCoverImagesByMBID(RELEASE_MBID, new CoverArtCallback() {
            @Override
            public void onCovertArtRetrievingFinished(Images images) {
                Assert.assertNotNull(images);
                Assert.assertEquals(3, images.size());
                images.forEach(image -> Assert.assertNotNull(image.getImageURL().getUrl()));
                lock.countDown();
                status = true;
            }

            @Override
            public void onCovertArtRetrievingFailed(ImageRetrievingException ex) {
                fail();
            }
        });

        lock.await(10, TimeUnit.SECONDS);
        Assert.assertTrue(status);
    }
}
