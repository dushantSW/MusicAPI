package com.dushantsw.services;

import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.CoverArtArchiveClient;
import com.dushantsw.integration.managers.MusicBrainzClient;
import com.dushantsw.integration.managers.WikipediaClient;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;
import com.dushantsw.integration.managers.exceptions.ImageRetrievingException;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.exceptions.MusicBrainzException;
import com.dushantsw.integration.managers.impl.DefaultCoverArtArchiveClient;
import com.dushantsw.integration.managers.impl.DefaultMusicBrainzClient;
import com.dushantsw.integration.managers.impl.DefaultWikipediaClient;
import org.springframework.stereotype.Component;

import javax.naming.ServiceUnavailableException;

/**
 * <code>MusicAPIService</code> is a service class which handles the communication
 * between the controller and the apis.
 *
 * @author dushantsw
 */
@Component
public class MusicAPIService {
    private MusicBrainzClient musicBrainzClient;
    private CoverArtArchiveClient coverArtArchiveClient;
    private WikipediaClient wikipediaClient;

    /**
     * Constructs a new instance of service.
     */
    public MusicAPIService() {
        this.musicBrainzClient = new DefaultMusicBrainzClient();
        this.coverArtArchiveClient = new DefaultCoverArtArchiveClient();
        this.wikipediaClient = new DefaultWikipediaClient();
    }

    /**
     * Mashes up all the three api to construct a new {@link Artist} for the {@code mbId}
     *
     * @param mbId Unique UUID of the artist provided by MusicBrainz API.
     * @return {@link Artist} if data is parsed and returned correctly.
     * @throws InvalidMBIDException        If the provided {@code mbId} is invalid.
     * @throws MusicBrainzException        If error occurs while downloading data from {@link MusicBrainzClient}
     * @throws AboutRetrievingException    If error occurs while downloading data from {@link MusicBrainzClient}
     * @throws ServiceUnavailableException If the service has exhausted all the retries and MusicBrainz Server
     *                                     is still unavailable.
     */
    public Artist getArtistByMBId(String mbId) throws InvalidMBIDException, MusicBrainzException,
            AboutRetrievingException, ServiceUnavailableException {
        // Load artist synchronously because we are dependant on musicbrainz data.
        Artist artist = musicBrainzClient.getArtistByMBId(mbId);
        if (artist == null)
            throw new ServiceUnavailableException("Music server is too slow");      // All retires are exhausted.

        if (!artist.getAlbums().isEmpty()) {
            // Download images of each album in a parallel stream.
            artist.getAlbums().parallelStream().forEach(album -> {
                try {
                    album.setImages(coverArtArchiveClient.getCoverImagesByMBID(album.getMbId().getId()));
                } catch (InvalidMBIDException | ImageRetrievingException e) {
                    // Ignore these errors.
                }
            });
            // Load wiki synchronously.
            artist.getAbout().setText(
                    wikipediaClient.getWikiPageOfArtistByName(artist.getAbout().getTitle()).getText()
            );
        }

        return artist;
    }
}
