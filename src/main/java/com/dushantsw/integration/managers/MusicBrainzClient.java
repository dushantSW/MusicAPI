package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.exceptions.MusicBrainzException;

/**
 * <code>MusicBrainzClient</code>
 *
 * @author dushantsw
 */
public interface MusicBrainzClient {
    Artist getArtistByMBId(final String mbId) throws InvalidMBIDException, MusicBrainzException;
}
