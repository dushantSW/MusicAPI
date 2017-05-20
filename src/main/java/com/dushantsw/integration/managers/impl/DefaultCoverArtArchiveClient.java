package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.managers.CoverArtArchiveClient;
import com.dushantsw.integration.managers.CoverArtCallback;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.utilities.Commons;
import com.mashape.unirest.http.Unirest;

/**
 * <code>DefaultCoverArtArchiveClient</code> provides default implementation for {@link CoverArtArchiveClient}
 * and its methods.
 *
 * @author dushantsw
 */
public class DefaultCoverArtArchiveClient implements CoverArtArchiveClient {

    @Override
    public void getCoverImagesByMBID(String mbId, CoverArtCallback callback) throws InvalidMBIDException {
        if (mbId == null || mbId.isEmpty())
            throw new InvalidMBIDException("Either null or empty mbid");

        if (!Commons.isUUIDValid(mbId))
            throw new InvalidMBIDException("Invalid mbid");

        Unirest.get(Commons.COVER_ART_BASE_URL.concat(mbId))
                .header("accept", "application/json")
                .asStringAsync(new CoverArtResponseCallback(callback));
    }
}
