package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.entities.Image;
import com.dushantsw.integration.entities.Images;
import com.dushantsw.integration.entities.exceptions.InvalidURLException;
import com.dushantsw.integration.managers.CoverArtCallback;
import com.dushantsw.integration.managers.exceptions.ImageRetrievingException;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <code>CoverArtResponseCallback</code> implements {@link Callback} to recieve http events
 * from the {@link com.mashape.unirest.http.Unirest} http calls.
 * <p>
 * </br>
 * Its takes in {@link CoverArtCallback} as constructor parameter. This callback is used to
 * call methods after parsing of http response has either succeeded or failed.
 *
 * @author dushantsw
 */
class CoverArtResponseCallback implements Callback<String> {
    private CoverArtCallback callback;

    /**
     * Constructs a new instance
     *
     * @param callback {@link CoverArtCallback} for method invocations after parsing.
     */
    CoverArtResponseCallback(final CoverArtCallback callback) {
        this.callback = callback;
    }

    @Override
    public void completed(HttpResponse<String> httpResponse) {
        if (httpResponse.getStatus() == 404) {
            callback.onCovertArtRetrievingFailed(new ImageRetrievingException("cover_art_not_found"));
            return;
        }

        if (httpResponse.getStatus() >= 200 && httpResponse.getStatus() <= 400) {
            CoverArt coverArt = new Gson().fromJson(httpResponse.getBody(), CoverArt.class);
            if (coverArt == null) {
                callback.onCovertArtRetrievingFailed(new ImageRetrievingException("json_parse_failed"));
                return;
            }

            CoverArtImage image = coverArt.images.stream().filter(ci -> ci.front).findAny().orElse(null);
            if (image == null) {
                callback.onCovertArtRetrievingFailed(new ImageRetrievingException("no_front_image"));
                return;
            }

            Images images = getImages(image);
            if (images == null) return;

            callback.onCovertArtRetrievingFinished(images);
        }
    }

    @Override
    public void failed(UnirestException e) {
        callback.onCovertArtRetrievingFailed((ImageRetrievingException) e);
    }

    @Override
    public void cancelled() {
        callback.onCovertArtRetrievingFailed(new ImageRetrievingException("request_cancelled"));
    }

    private Images getImages(CoverArtImage image) {
        Images images = new Images();
        try {
            images.addNewImage(Image.ImageType.Original, image.image);
            images.addNewImage(Image.ImageType.Large, image.thumbnails.get("large"));
            images.addNewImage(Image.ImageType.Small, image.thumbnails.get("small"));
        } catch (InvalidURLException e) {
            callback.onCovertArtRetrievingFailed(new ImageRetrievingException(e.getMessage(), e));
            return null;
        }
        return images;
    }

    /**
     * <code>CoverArt</code>, a data transfer object for parsing JSON data from
     * CovertArtArchive.
     *
     * @author dushantsw
     */
    private class CoverArt implements Serializable {
        List<CoverArtImage> images;
    }

    /**
     * <code>CoverArtImage</code>, a data transfer object for parsing JSON data for
     * each image in CoverArtArchive images response.
     */
    private class CoverArtImage implements Serializable {
        boolean front;
        String image;
        Map<String, String> thumbnails;
    }
}

