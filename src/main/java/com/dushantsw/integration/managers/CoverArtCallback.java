package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.Images;
import com.dushantsw.integration.managers.exceptions.ImageRetrievingException;

/**
 * <code>CoverArtCallback</code> is a interface which can be used to receive events
 * when downloading cover art images data from CoverArtArchive.
 *
 * @author dushantsw
 */
public interface CoverArtCallback {
    /**
     * Method is called when the data has been successfully downloaded and parsed.
     *
     * @param images A list of {@link com.dushantsw.integration.entities.Image} containing
     *               every type of image and its {@link com.dushantsw.integration.entities.ImageUrl}
     * @see com.dushantsw.integration.entities.Image.ImageType
     */
    void OnCovertArtRetrievingFinished(Images images);

    /**
     * Method is called when data is not found or parsing exceptions happen
     *
     * @param ex {@link ImageRetrievingException} containing messages and causes.
     */
    void OnCovertArtRetrievingFailed(ImageRetrievingException ex);
}
