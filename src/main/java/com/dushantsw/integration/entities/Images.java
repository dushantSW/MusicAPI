package com.dushantsw.integration.entities;

import com.dushantsw.integration.entities.exceptions.InvalidURLException;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * <code>Images</code>
 *
 * @author dushantsw
 */
public class Images extends LinkedList<Image> {
    public void addNewImage(Image.ImageType type, String url) throws InvalidURLException {
        this.add(Image.ofValues(type, url));
    }

    public void removeImagesWithType(Image.ImageType type) {
        removeAll(stream().filter(image -> image.getType() == type).collect(Collectors.toList()));
    }

    public void removeImagesWithUrl(ImageUrl url) {
        removeAll(stream().filter(image -> image.getImageURL().equals(url)).collect(Collectors.toList()));
    }

    public void removeImage(Image image) {
        remove(image);
    }
}
