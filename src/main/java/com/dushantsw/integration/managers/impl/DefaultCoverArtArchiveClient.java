package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.entities.Image;
import com.dushantsw.integration.entities.Images;
import com.dushantsw.integration.entities.exceptions.InvalidURLException;
import com.dushantsw.integration.managers.CoverArtArchiveClient;
import com.dushantsw.integration.managers.exceptions.ImageRetrievingException;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.utilities.Commons;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * <code>DefaultCoverArtArchiveClient</code> provides default implementation for {@link CoverArtArchiveClient}
 * and its methods.
 *
 * @author dushantsw
 */
public class DefaultCoverArtArchiveClient implements CoverArtArchiveClient {

    @Override
    public Images getCoverImagesByMBID(String mbId) throws InvalidMBIDException, ImageRetrievingException {
        if (mbId == null || mbId.isEmpty())
            throw new InvalidMBIDException("Either null or empty mbid");

        if (!Commons.isUUIDValid(mbId))
            throw new InvalidMBIDException("Invalid mbid");

        HttpResponse<String> httpResponse;
        try {
            httpResponse = Unirest.get(Commons.COVER_ART_BASE_URL.concat(mbId))
                    .header("accept", "application/json").asString();
        } catch (UnirestException e) {
            throw new ImageRetrievingException(e);
        }

        if (httpResponse.getStatus() == 404) {
            throw new ImageRetrievingException("cover_art_not_found for id: " + mbId);
        }

        if (httpResponse.getStatus() >= 200 && httpResponse.getStatus() <= 400) {
            return getImages(httpResponse);
        }

        return null;
    }

    private Images getImages(HttpResponse<String> httpResponse) throws ImageRetrievingException {
        CoverArt coverArt = new Gson().fromJson(httpResponse.getBody(), CoverArt.class);
        if (coverArt == null) {
            throw new ImageRetrievingException("json_parse_failed");
        }

        CoverArtImage image = coverArt.images.stream().filter(ci -> ci.front).findAny().orElse(null);
        if (image == null) {
            throw new ImageRetrievingException("no_front_image");
        }

        return getImages(image);
    }

    private Images getImages(CoverArtImage image) {
        Images images = new Images();
        try {
            images.addNewImage(Image.ImageType.Original, image.image);
            images.addNewImage(Image.ImageType.Large, image.thumbnails.get("large"));
            images.addNewImage(Image.ImageType.Small, image.thumbnails.get("small"));
        } catch (InvalidURLException e) {
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
