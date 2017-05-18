package com.dushantsw.integration.models;

import com.dushantsw.integration.models.exceptions.InvalidURLException;
import lombok.Data;

/**
 * <code>Image</code>
 *
 * @author dushantsw
 */
@Data
public class Image {
    public enum ImageType {
        Original,
        Small,
        Large
    }

    private ImageType type;
    private ImageUrl url;

    private Image(ImageType type, ImageUrl url) {
        this.type = type;
        this.url = url;
    }

    public static Image ofValues(ImageType type, String url) throws InvalidURLException {
        if (url == null || url.isEmpty())
            throw new IllegalArgumentException("url is empty");
        if (type == null)
            throw new IllegalArgumentException("Type of image is required");

        return new Image(type, ImageUrl.ofURL(url));
    }
}
