package com.dushantsw.integration.entities;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * <code>Artist</code> an data transfer object defining the model of an artist
 *
 * @author dushantsw
 */
@Data
@Builder
public class Artist {
    public enum ArtistType {
        GROUP, PERSON
    }

    private MBID mbId;
    private String title;
    private ArtistType type;
    private List<Album> albums;
    private About about;
}
