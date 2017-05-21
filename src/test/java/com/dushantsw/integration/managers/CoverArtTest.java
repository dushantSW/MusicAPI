package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.Images;
import com.dushantsw.integration.managers.exceptions.ImageRetrievingException;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.impl.DefaultCoverArtArchiveClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <code>CoverArtTest</code>
 *
 * @author dushantsw
 */
public class CoverArtTest {
    private CoverArtArchiveClient client;

    @Before
    public void beforeTests() {
        client = new DefaultCoverArtArchiveClient();
    }

    @Test(expected = InvalidMBIDException.class)
    public void testNullOrEmptyMbId() throws Throwable {
        client.getCoverImagesByMBID(null);
    }

    @Test(expected = InvalidMBIDException.class)
    public void testInvalidUUIDMBID() throws Throwable {
        client.getCoverImagesByMBID("asdas-asdasgsgsdgsdgsdgsdgsgsdgs");
    }

    @Test(expected = ImageRetrievingException.class)
    public void testNotAvailableCoverArt() throws Throwable {
        // Checked uuid, it should throw 404 and get caught by client.
        String INVALID_MBID = "1b023e01-4d56-387b-8758-8678046e4cef";
        Images images = client.getCoverImagesByMBID(INVALID_MBID);
        Assert.assertNotNull(images);
        Assert.assertEquals(3, images.size());
        images.forEach(image -> Assert.assertNotNull(image.getImageURL().getUrl()));
    }

    @Test
    public void testSuccessCoverDownload() throws Throwable {
        String RELEASE_MBID = "1b022e01-4da6-387b-8658-8678046e4cef";
        System.out.printf("Release mbid: " + RELEASE_MBID);

        Images images = client.getCoverImagesByMBID(RELEASE_MBID);
        Assert.assertNotNull(images);
        Assert.assertEquals(3, images.size());
        images.forEach(image -> Assert.assertNotNull(image.getImageURL().getUrl()));
    }
}
