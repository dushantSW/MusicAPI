package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.managers.CoverArtCallback;
import com.dushantsw.integration.managers.WikipediaCallback;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <code>WikipediaResponseCallback</code> implements {@link Callback} to recieve http events
 * from the {@link com.mashape.unirest.http.Unirest} http calls.
 * <p>
 * </br>
 * Its takes in {@link WikipediaCallback} as constructor parameter. This callback is used to
 * call methods after parsing of http response has either succeeded or failed.
 *
 * @author dushantsw
 */
class WikipediaResponseCallback implements Callback<String> {
    private final WikipediaCallback callback;

    /**
     * Constructs a new instance
     *
     * @param callback {@link WikipediaCallback} for events method-invocation.
     */
    WikipediaResponseCallback(WikipediaCallback callback) {
        this.callback = callback;
    }

    @Override
    public void completed(HttpResponse<String> httpResponse) {
        if (httpResponse.getStatus() == 200 || httpResponse.getStatus() == 201) {
            JsonParser parser = new JsonParser();

            JsonObject mainObject = parser.parse(httpResponse.getBody()).getAsJsonObject();
            if (mainObject == null) {
                callback.onWikipediaInformationFailed(new AboutRetrievingException("parsing on failed"));
                return;
            }

            JsonObject pages = mainObject.getAsJsonObject("query").getAsJsonObject("pages");
            for (Map.Entry<String, JsonElement> entry : pages.entrySet()) {
                JsonObject element = entry.getValue().getAsJsonObject();
                if (element.get("missing") != null) {
                    callback.onWikipediaInformationFailed(new AboutRetrievingException("Page missing"));
                    return;
                }

                About about = About.builder().text(element.get("extract").getAsString()).build();
                callback.onWikipediaInformationDownloaded(about);
            }
        }
    }

    @Override
    public void failed(UnirestException e) {
        callback.onWikipediaInformationFailed((AboutRetrievingException) e);
    }

    @Override
    public void cancelled() {
        callback.onWikipediaInformationFailed(new AboutRetrievingException("Request cancelled"));
    }
}
