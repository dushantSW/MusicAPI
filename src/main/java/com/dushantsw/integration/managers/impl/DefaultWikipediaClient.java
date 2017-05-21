package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.managers.WikipediaClient;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;
import com.dushantsw.utilities.Commons;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * <code>DefaultWikipediaClient</code> provides default implementation for {@link WikipediaClient}
 * and its methods.
 *
 * @author dushantsw
 */
public class DefaultWikipediaClient implements WikipediaClient {
    @Override
    public About getWikiPageOfArtistByName(String name) throws AboutRetrievingException {
        if (name == null || name.isEmpty())
            throw new IllegalArgumentException("Name is either null or empty");

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

        return ParseResponse(httpResponse);
    }

    private About ParseResponse(HttpResponse<String> httpResponse) throws AboutRetrievingException {
        if (httpResponse.getStatus() == 200 || httpResponse.getStatus() == 201) {
            JsonParser parser = new JsonParser();

            JsonObject mainObject = parser.parse(httpResponse.getBody()).getAsJsonObject();
            if (mainObject == null)
                throw new AboutRetrievingException("parsing on failed");

            JsonObject pages = mainObject.getAsJsonObject("query").getAsJsonObject("pages");
            JsonObject element = pages.entrySet().iterator().next().getValue().getAsJsonObject();
            if (element.get("missing") != null)
                throw new AboutRetrievingException("Page missing");

            return About.builder().text(element.get("extract").getAsString()).build();
        }

        return null;
    }
}
