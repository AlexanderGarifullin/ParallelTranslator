package org.tba.paralleltranslator.interfaces;

/**
 * Interface for translation clients that perform text translation.
 * Implementations of this interface interact with different translation services.
 */
public interface TranslationClient {

    /**
     * Translates the given text from the source language to the target language.
     *
     * @param text the text to translate
     * @param sourceLang the source language code
     * @param targetLang the target language code
     * @return the translated text
     * @throws RuntimeException if there is an error during translation
     */
    String translate(String text, String sourceLang, String targetLang);
}
