package com.dushantsw.integration.managers;

import com.dushantsw.integration.entities.About;
import com.dushantsw.integration.managers.exceptions.AboutRetrievingException;

/**
 * <code>WikipediaCallback</code> is a interface which can be used to receive events
 * when getting wikipedia page.
 *
 * @author dushantsw
 */
public interface WikipediaCallback {
    /**
     * Method is called when the data has been successfully downloaded and parsed.
     *
     * @param about {@link About} containing extracted information from wiki page.
     */
    void onWikipediaInformationDownloaded(About about);

    /**
     * Method is called when page is not found or parsing exceptions happen.
     *
     * @param ex {@link AboutRetrievingException} containing messages and causes.
     */
    void onWikipediaInformationFailed(AboutRetrievingException ex);
}
