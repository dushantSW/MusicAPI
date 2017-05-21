package com.dushantsw.services;

import com.dushantsw.integration.entities.Album;
import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.entities.MBID;
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
 * <code>MusicAPIService</code>
 *
 * @author dushantsw
 */
@Component
public class MusicAPIService {
    private MusicBrainzClient musicBrainzClient;
    private CoverArtArchiveClient coverArtArchiveClient;
    private WikipediaClient wikipediaClient;

    /**
     *
     */
    public MusicAPIService() {
        this.musicBrainzClient = new DefaultMusicBrainzClient();
        this.coverArtArchiveClient = new DefaultCoverArtArchiveClient();
        this.wikipediaClient = new DefaultWikipediaClient();
    }

    public Artist getArtistByMBId(String mbId) throws InvalidMBIDException, MusicBrainzException, AboutRetrievingException, ServiceUnavailableException {
        Artist artist = musicBrainzClient.getArtistByMBId(mbId);
        if (artist == null)
            throw new ServiceUnavailableException("Music server is too slow");

        if (!artist.getAlbums().isEmpty()) {
            artist.getAlbums().parallelStream().forEach(album -> {
                try {
                    album.setImages(coverArtArchiveClient.getCoverImagesByMBID(album.getMbId().getId()));
                } catch (InvalidMBIDException | ImageRetrievingException e) {
                    e.printStackTrace(); // TODO: Better log.
                }
            });
            artist.getAbout().setText(
                    wikipediaClient.getWikiPageOfArtistByName(artist.getAbout().getTitle()).getText()
            );
            return artist;
        }

        return null;
    }
}
