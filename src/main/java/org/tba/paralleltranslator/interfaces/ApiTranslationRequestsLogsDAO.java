package org.tba.paralleltranslator.interfaces;

public interface ApiTranslationRequestsLogsDAO {
    void save(String clientIp, String text, String translatedText);
}
