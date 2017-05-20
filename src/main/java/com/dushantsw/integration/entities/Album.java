package com.dushantsw.integration.entities;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <code>Album</code>
 *
 * @author dushantsw
 */
@Data
@Builder
public class Album implements Serializable {
    public enum AlbumType {
        ALBUM, PERSON
    }

    private String mbId;
    private AlbumTitle title;
    private AlbumType type;
    private Images images;
}
