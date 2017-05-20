package com.dushantsw.integration.managers;

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
     * @param name     of the page.
     * @param callback {@link WikipediaCallback} for events.
     */
    void getWikiPageOfArtistByName(final String name, final WikipediaCallback callback);
}
