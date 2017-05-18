package com.dushantsw.integration.models;

import lombok.Data;

import java.util.List;

/**
 * <code>Artist</code> an data transfer object defining the model of an artist
 *
 * @author dushantsw
 */
@Data
public class Artist {
    private List<Album> albums;
    private About about;
}
