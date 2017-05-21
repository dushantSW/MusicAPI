package com.dushantsw.utilities;

import java.util.UUID;

/**
 * <code>Commons</code> contains variables and methods which are commonly used
 * by the system.
 *
 * @author dushantsw
 */
public class Commons {
    /**
     * Base url to the wikipedia api
     */
    public static final String WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/w/api.php";
    /**
     * Base url to the music brainz url
     */
    public static final String MUSICBRAINZ_BASE_URL = "http://musicbrainz.org/ws/2/artist/";
    /**
     * Base URL to the cover art archive url.
     */
    public static final String COVER_ART_BASE_URL = "http://coverartarchive.org/release-group/";

    /**
     * Checks if the given UUID is parsable with {@link UUID} class.
     *
     * @param uuid string format uuid.
     * @return True if parsable else false.
     */
    public static boolean isUUIDValid(String uuid) {
        try {
            //noinspection ResultOfMethodCallIgnored
            UUID.fromString(uuid);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
