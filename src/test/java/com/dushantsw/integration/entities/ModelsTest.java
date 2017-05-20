package com.dushantsw.integration.entities;

import com.dushantsw.integration.entities.exceptions.InvalidURLException;
import org.junit.Assert;
import org.junit.Test;

/**
 * <code>ModelsTest</code>
 *
 * @author dushantsw
 */
public class ModelsTest {

    @Test(expected = InvalidURLException.class)
    public void failedValidationImageUrl() throws InvalidURLException {
        ImageUrl.ofURL("ftp://something:21");
    }

    @Test
    public void passedValidImageUrl() throws InvalidURLException {
        String url = "http://somewhere.com/anything.jpg";
        ImageUrl imageUrl = ImageUrl.ofURL(url);
        Assert.assertEquals(url, imageUrl.getUrl());
    }

    @Test
    public void testImagesList() throws InvalidURLException {
        String url = "http://somewhere.com/anything.jpg";

        Images images = new Images();

        // Insert one original
        images.addNewImage(Image.ImageType.Original, url);
        Assert.assertEquals(Image.ImageType.Original, images.get(0).getType());
        Assert.assertEquals(url, images.get(0).getImageURL().getUrl());

        // Insert one small
        images.addNewImage(Image.ImageType.Small, url);
        Assert.assertEquals(Image.ImageType.Small, images.get(1).getType());
        Assert.assertEquals(url, images.get(1).getImageURL().getUrl());

        // Insert another small
        images.addNewImage(Image.ImageType.Small, url);
        Assert.assertEquals(Image.ImageType.Small, images.get(2).getType());
        Assert.assertEquals(url, images.get(2).getImageURL().getUrl());

        // Remove small type
        images.removeImagesWithType(Image.ImageType.Small);
        Assert.assertEquals(1, images.size());
    }
}
