package com.dushantsw.integration.managers;

import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;

/**
 * Provides access to the Cover Art Archive.
 *
 * @see <a href="http://musicbrainz.org/doc/Cover_Art_Archive/API">http://musicbrainz.org/doc/Cover_Art_Archive/API</a>
 */
public interface CoverArtArchiveClient {

    /**
     * Sends an asynchronous request to CoverArtArchive API to get images for the
     * given {@code mbId}
     *
     * @param mbId     UUID of MusicBrainz as string
     * @param callback {@link CoverArtCallback} for receiving events
     * @throws InvalidMBIDException if {@code mbId} is null or invalid.
     */
    void getCoverImagesByMBID(final String mbId, final CoverArtCallback callback) throws InvalidMBIDException;
}
