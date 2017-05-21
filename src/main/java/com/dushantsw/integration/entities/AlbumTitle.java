package com.dushantsw.integration.entities;

import com.dushantsw.integration.entities.serializers.AlbumTitleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

/**
 * <code>AlbumTitle</code> is value-type object defining the model of an
 * album title.
 *
 * @author dushantsw
 */
@Data
@JsonSerialize(using = AlbumTitleSerializer.class)
public class AlbumTitle {
    private String title;

    /**
     * Constructs a new instance with given title
     *
     * @param title of the album.
     */
    private AlbumTitle(String title) {
        this.title = title;
    }

    /**
     * Checks and provides a new instance with the given title .
     *
     * @param title of the album
     * @return {@link AlbumTitle}
     */
    public static AlbumTitle OfTitle(String title) {
        if (title == null || title.isEmpty())
            throw new IllegalArgumentException("title is null or empty");

        return new AlbumTitle(title);
    }
}
