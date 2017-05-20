package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.managers.WikipediaCallback;
import com.dushantsw.integration.managers.WikipediaClient;
import com.dushantsw.utilities.Commons;
import com.mashape.unirest.http.Unirest;

/**
 * <code>DefaultWikipediaClient</code> provides default implementation for {@link WikipediaClient}
 * and its methods.
 *
 * @author dushantsw
 */
public class DefaultWikipediaClient implements WikipediaClient {
    @Override
    public void getWikiPageOfArtistByName(String name, WikipediaCallback callback) {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name is either null or empty");

        Unirest.get(Commons.WIKIPEDIA_BASE_URL)
                .queryString("action", "query")
                .queryString("format", "json")
                .queryString("prop", "extracts")
                .queryString("exintro", true)
                .queryString("titles", name)
                .asStringAsync(new WikipediaResponseCallback(callback));
    }
}
