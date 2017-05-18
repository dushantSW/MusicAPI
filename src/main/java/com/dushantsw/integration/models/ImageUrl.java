package com.dushantsw.integration.models;

import com.dushantsw.integration.models.exceptions.InvalidURLException;
import lombok.Data;

/**
 * <code>ImageUrl</code>
 *
 * @author dushantsw
 */
@Data
public class ImageUrl {
    private String url;
    private ImageUrl(String url) {
        this.url = url;
    }

    public static ImageUrl ofURL(String url) throws InvalidURLException {
        if(url == null || url.isEmpty())
            throw new IllegalArgumentException("Url is empty or null");
        if (!isUrlValid(url))
            throw new InvalidURLException();

        return new ImageUrl(url);
    }

    private static boolean isUrlValid(String url) {
        return false;
    }
}
