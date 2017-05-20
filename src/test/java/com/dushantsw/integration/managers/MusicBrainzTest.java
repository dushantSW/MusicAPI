package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.impl.DefaultMusicBrainzClient;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <code>MusicBrainzTest</code>
 *
 * @author dushantsw
 */
public class MusicBrainzTest {
    private MusicBrainzClient client;

    @Before
    public void beforeTests() {
        client = new DefaultMusicBrainzClient();
    }

    @Test
    public void testSuccessMusicBrainz() throws Throwable {
        String mbId = "5b11f4ce-a62d-471e-81fc-a69a8278c7da";
        Artist artist = client.getArtistByMBId(mbId);
        assertNotNull(artist);
        assertEquals(mbId, artist.getMbId());
        assertFalse(artist.getAlbums().isEmpty());
        assertFalse(artist.getAbout().getTitle().isEmpty());
    }
}
