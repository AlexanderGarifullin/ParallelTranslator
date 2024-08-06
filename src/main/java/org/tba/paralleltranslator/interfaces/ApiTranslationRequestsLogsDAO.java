package org.tba.paralleltranslator.interfaces;

/**
 * Data Access Object (DAO) interface for saving translation request logs.
 * Provides methods to persist translation request details.
 */
public interface ApiTranslationRequestsLogsDAO {

    /**
     * Saves the translation request details including client IP, original text, and translated text.
     *
     * @param clientIp the IP address of the client making the request
     * @param text the original text to be translated
     * @param translatedText the translated text
     */
    void save(String clientIp, String text, String translatedText);
}
