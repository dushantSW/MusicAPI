package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.exceptions.MusicBrainzException;

/**
 * Provides access to the MusicBrainz API.
 *
 * @author dushantsw
 */
public interface MusicBrainzClient {

    /**
     * Requests artist information from the MusicBrainz API.
     *
     * @param mbId Unique artist uuid.
     * @return {@link Artist}
     * @throws InvalidMBIDException if the {@code mbId} is null, empty or invalid.
     * @throws MusicBrainzException if error occurred while parsing data or retries exhausted.
     * @see com.dushantsw.ContextRefreshListener for number of retries.
     */
    Artist getArtistByMBId(final String mbId) throws InvalidMBIDException, MusicBrainzException;
}
