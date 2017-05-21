package com.dushantsw.integration.managers.impl;

import com.dushantsw.integration.cache.CacheStorage;
import com.dushantsw.integration.entities.*;
import com.dushantsw.integration.managers.MusicBrainzClient;
import com.dushantsw.integration.managers.exceptions.InvalidMBIDException;
import com.dushantsw.integration.managers.exceptions.MusicBrainzException;
import com.dushantsw.utilities.Commons;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <code>DefaultMusicBrainzClient</code> provides default implementation for {@link MusicBrainzClient}
 * and its methods.
 *
 * @author dushantsw
 */
public class DefaultMusicBrainzClient implements MusicBrainzClient {
    private CacheStorage<Artist> cacheStorage;

    public DefaultMusicBrainzClient(CacheStorage<Artist> cacheStorage) {
        this.cacheStorage = cacheStorage;
    }

    public DefaultMusicBrainzClient() {}

    @Override
    public Artist getArtistByMBId(String mbId) throws InvalidMBIDException, MusicBrainzException {
        if (mbId == null || mbId.isEmpty())
            throw new InvalidMBIDException("Null or empty mbId");

        if (!Commons.isUUIDValid(mbId))
            throw new InvalidMBIDException("invalid mbid");

        Artist artist;
        if (cacheStorage != null && cacheStorage.isStorageAvailable()) {
            try {
                artist = cacheStorage.getFromStorage(MBID.ofMbId(mbId));
            } catch (IOException e) {
                throw new MusicBrainzException(e);
            }
            if (artist == null)
                artist = getArtistFromNetwork(mbId);
        } else {
            artist = getArtistFromNetwork(mbId);
        }

        return artist;
    }

    private Artist getArtistFromNetwork(String mbId) throws MusicBrainzException, InvalidMBIDException {
        HttpResponse<String> httpResponse;
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

        Artist artist = null;
        if (httpResponse.getStatus() == 200 || httpResponse.getStatus() == 201) {
            MusicBrainz artistBrainz = new Gson().fromJson(httpResponse.getBody(), MusicBrainz.class);
            artist = parseMusicBrainz(artistBrainz);

            // Store the artist into storage
            if (cacheStorage != null && cacheStorage.isStorageAvailable()) {
                try {
                    cacheStorage.storeIntoStorage(artist.getMbId(), artist);
                } catch (JsonProcessingException | UnsupportedEncodingException e) {
                    e.printStackTrace();            // Ignore errors or log them better.
                }
            }
        }

        return artist;
    }

    private Artist parseMusicBrainz(MusicBrainz artistBrainz) throws InvalidMBIDException {
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
                .map(releaseGroup -> {
                    try {
                        return createAlbum(releaseGroup);
                    } catch (InvalidMBIDException e) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());

        // Artist
        return Artist.builder()
                .mbId(MBID.ofMbId(artistBrainz.id))
                .title(artistBrainz.name)
                .type(Artist.ArtistType.valueOf(artistBrainz.type.toUpperCase()))
                .albums(albums)
                .about(About.builder().title(titleName).build())
                .build();
    }

    private Album createAlbum(ReleaseGroup releaseGroup) throws InvalidMBIDException {
        return Album.builder()
                .title(AlbumTitle.OfTitle(releaseGroup.title))
                .mbId(MBID.ofMbId(releaseGroup.id)).build();
    }

    private class MusicBrainz implements Serializable {
        String id;
        String name;
        String type;
        @SerializedName("release-groups")
        List<ReleaseGroup> groups;
        List<JsonObject> relations;
    }

    private class ReleaseGroup implements Serializable {
        String id;
        String title;
    }
}
