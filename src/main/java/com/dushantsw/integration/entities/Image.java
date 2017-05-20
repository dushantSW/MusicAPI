package com.dushantsw.integration.entities;

import com.dushantsw.integration.entities.exceptions.InvalidURLException;
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
    private ImageUrl imageURL;

    private Image(ImageType type, ImageUrl imageURL) {
        this.type = type;
        this.imageURL = imageURL;
    }

    public static Image ofValues(ImageType type, String url) throws InvalidURLException {
        if (url == null || url.isEmpty())
            throw new IllegalArgumentException("imageURL is empty");
        if (type == null)
            throw new IllegalArgumentException("Type of image is required");

        return new Image(type, ImageUrl.ofURL(url));
    }
}
