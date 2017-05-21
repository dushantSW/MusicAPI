package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.entities.Album;
import com.dushantsw.integration.entities.AlbumTitle;
import com.dushantsw.integration.entities.Artist;
import com.dushantsw.integration.managers.MusicBrainzClient;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.exceptions.MusicBrainzException;
import com.dushantsw.utilities.Commons;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <code>DefaultMusicBrainzClient</code>
 *
 * @author dushantsw
 */
public class DefaultMusicBrainzClient implements MusicBrainzClient {
    @Override
    public Artist getArtistByMBId(String mbId) throws InvalidMBIDException, MusicBrainzException {
        if (mbId == null || mbId.isEmpty())
            throw new InvalidMBIDException("Null or empty mbId");

        if (!Commons.isUUIDValid(mbId))
            throw new InvalidMBIDException("invalid mbid");

        HttpResponse<String> httpResponse = null;

        try {
            httpResponse = Unirest.get(Commons.MUSICBRAINZ_BASE_URL.concat(mbId))
                    .queryString("fmt", "json")
                    .queryString("inc", "url-rels+release-groups").asString();
        } catch (UnirestException e) {
            throw new MusicBrainzException(e);
        }

        if (httpResponse.getStatus() == 400)
            throw new MusicBrainzException("Invalid mbid");

        if (httpResponse.getStatus() == 404)
            throw new MusicBrainzException("No artist found with given mbId");

        if (httpResponse.getStatus() == 200 || httpResponse.getStatus() == 201) {
            MusicBrainz artistBrainz = new Gson().fromJson(httpResponse.getBody(), MusicBrainz.class);
            return parseMusicBrainz(artistBrainz);
        }

        return null;
    }

    private Artist parseMusicBrainz(MusicBrainz artistBrainz) {
        // Get wikipedia url
        String titleName = null;
        JsonObject wikipediaObject = artistBrainz.relations.stream().filter(jsonObject ->
                jsonObject.get("type").getAsString().equals("wikipedia")).findAny().orElse(null);
        if (wikipediaObject != null) {
            String[] urlSplit = wikipediaObject.getAsJsonObject("url")
                    .get("resource").getAsString().split("/");
            titleName = urlSplit[urlSplit.length - 1];
        }

        // Get all albums and their urls
        List<Album> albums = artistBrainz.groups.stream()
                .map(releaseGroup -> Album.builder()
                        .title(AlbumTitle.OfTitle(releaseGroup.title))
                        .mbId(releaseGroup.id)
                        .type(Album.AlbumType.valueOf(releaseGroup.primaryType.toUpperCase())).build())
                .collect(Collectors.toList());

        // Artist
        return Artist.builder()
                .mbId(artistBrainz.id)
                .title(artistBrainz.name)
                .group(artistBrainz.group)
                .albums(albums)
                .about(About.builder().title(titleName).build())
                .build();
    }

    private class MusicBrainz implements Serializable {
        String id;
        String name;
        String group;
        @SerializedName("release-groups")
        List<ReleaseGroup> groups;
        List<JsonObject> relations;
    }

    private class ReleaseGroup implements Serializable {
        @SerializedName("primary-type-id")
        String id;
        String title;
        @SerializedName("primary-type")
        String primaryType;
    }
}