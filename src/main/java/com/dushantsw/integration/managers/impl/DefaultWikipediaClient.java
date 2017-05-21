package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.cache.CacheStorage;
import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.managers.WikipediaClient;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;
import com.dushantsw.utilities.Commons;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * <code>DefaultWikipediaClient</code> provides default implementation for {@link WikipediaClient}
 * and its methods.
 *
 * @author dushantsw
 */
public class DefaultWikipediaClient implements WikipediaClient {
    private CacheStorage<About> cacheStorage;

    public DefaultWikipediaClient(CacheStorage<About> storage) {
        this.cacheStorage = storage;
    }

    @Override
    public About getWikiPageOfArtistByName(String name) throws AboutRetrievingException {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name is either null or empty");

        About about = null;
        if (cacheStorage != null && cacheStorage.isStorageAvailable()) {
            try {
                about = cacheStorage.getFromStorage(name);
            } catch (IOException e) {
                e.printStackTrace();        // Ignore this error
            }

            if (about == null) about = loadAboutFromNetwork(name);
        } else {
            about = loadAboutFromNetwork(name);
        }

        return about;
    }

    private About loadAboutFromNetwork(String name) throws AboutRetrievingException {
        About about;
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = Unirest.get(Commons.WIKIPEDIA_BASE_URL)
                    .queryString("action", "query")
                    .queryString("format", "json")
                    .queryString("prop", "extracts")
                    .queryString("exintro", true)
                    .queryString("titles", name).asString();
        } catch (UnirestException e) {
            throw new AboutRetrievingException(e);
        }

        about = ParseResponse(name, httpResponse);
        return about;
    }

    private About ParseResponse(String name, HttpResponse<String> httpResponse) throws AboutRetrievingException {
        About about = null;
        if (httpResponse.getStatus() == 200 || httpResponse.getStatus() == 201) {
            JsonParser parser = new JsonParser();

            JsonObject mainObject = parser.parse(httpResponse.getBody()).getAsJsonObject();
            if (mainObject == null)
                throw new AboutRetrievingException("parsing on failed");

            JsonObject pages = mainObject.getAsJsonObject("query").getAsJsonObject("pages");
            JsonObject element = pages.entrySet().iterator().next().getValue().getAsJsonObject();
            if (element.get("missing") != null)
                throw new AboutRetrievingException("Page missing");

            about = About.builder().text(element.get("extract").getAsString()).build();

            // Store into cache
            if (cacheStorage != null && cacheStorage.isStorageAvailable()) {
                try {
                    cacheStorage.storeIntoStorage(name, about);
                } catch (JsonProcessingException | UnsupportedEncodingException e) {
                    e.printStackTrace(); // TODO: Log this error.
                }
            }
        }

        return about;
    }
}
