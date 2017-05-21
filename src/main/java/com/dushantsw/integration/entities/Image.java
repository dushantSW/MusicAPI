package com.dushantsw.integration.entities;

import com.dushantsw.integration.entities.exceptions.InvalidURLException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@SuppressWarnings("WeakerAccess")
public class Image {
    public enum ImageType {
        Original,
        Small,
        Large
    }

    private ImageType type;
    private @JsonProperty("url")
    ImageUrl imageURL;

    // Jackson destroyed the purpose of class.
    protected Image() {}

    private Image(ImageType type, ImageUrl imageURL) {
        this.type = type;
        this.imageURL = imageURL;
    }

    /**
     * Constructs a new instance of {@link Image}
     *
     * @param type {@link ImageType} of the image
     * @param url  string url
     * @return {@link Image}
     * @throws InvalidURLException if the url is empty, invalid or type is empty.
     */
    public static Image ofValues(ImageType type, String url) throws InvalidURLException {
        if (url == null || url.isEmpty())
            throw new IllegalArgumentException("imageURL is empty");
        if (type == null)
            throw new IllegalArgumentException("Type of image is required");

        return new Image(type, ImageUrl.ofURL(url));
    }
}
