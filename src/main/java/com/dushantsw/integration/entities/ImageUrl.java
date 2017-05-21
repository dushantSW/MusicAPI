package com.dushantsw.integration.entities;

import com.dushantsw.integration.entities.exceptions.InvalidURLException;
import com.dushantsw.integration.entities.serializers.ImageUrlSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.commons.validator.UrlValidator;

@Data
@JsonSerialize(using = ImageUrlSerializer.class)
@SuppressWarnings("WeakerAccess")
public class ImageUrl {
    private String url;
    private ImageUrl(String url) {
        this.url = url;
    }

    /**
     * Creates a new instance of ImageUrl
     *
     * @param url to the image
     * @return {@link ImageUrl}
     * @throws InvalidURLException if the imageURL does not passes validation.
     */
    public static ImageUrl ofURL(String url) throws InvalidURLException {
        if (url == null || url.isEmpty())
            throw new IllegalArgumentException("Url is empty or null");
        if (!isUrlValid(url))
            throw new InvalidURLException();

        return new ImageUrl(url);
    }

    private static boolean isUrlValid(String url) {
        return new UrlValidator(new String[]{"http", "https"}).isValid(url);
    }
}
