package com.dushantsw.integration.entities;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * <code>Album</code>
 *
 * @author dushantsw
 */
@Data
@Builder
public class Album implements Serializable {
    private MBID mbId;
    private AlbumTitle title;
    private Images images;
}
