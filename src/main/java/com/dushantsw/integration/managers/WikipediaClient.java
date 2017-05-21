package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;

/**
 * Provides access to the Wikipedia.
 *
 * @author dushantsw
 */
public interface WikipediaClient {

    /**
     * Sends an asynchronous request to Wikipedia API to get images for the
     * given {@code name}
     *
     * @param callback {@link WikipediaCallback} for events.
     * @param name     of the page.
     */
    About getWikiPageOfArtistByName(final String name) throws AboutRetrievingException;
}
