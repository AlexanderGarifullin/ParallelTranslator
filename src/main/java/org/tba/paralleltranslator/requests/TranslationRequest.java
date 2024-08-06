package org.tba.paralleltranslator.requests;

/**
 * Represents a request for translation.
 * Contains the text to be translated, the source language code, and the target language code.
 */
public class TranslationRequest {
    private String text;
    private String sourceLang;
    private String targetLang;

    /**
     * Default constructor.
     */
    public TranslationRequest() {}

    /**
     * Constructs a new TranslationRequest with the specified text, source language, and target language.
     *
     * @param text the text to be translated
     * @param sourceLang the source language code
     * @param targetLang the target language code
     */
    public TranslationRequest(String text, String sourceLang, String targetLang) {
        this.text = text;
        this.sourceLang = sourceLang;
        this.targetLang = targetLang;
    }

    /**
     * Gets the text to be translated.
     *
     * @return the text to be translated
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text to be translated.
     *
     * @param text the text to be translated
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets the source language code.
     *
     * @return the source language code
     */
    public String getSourceLang() {
        return sourceLang;
    }

    /**
     * Sets the source language code.
     *
     * @param sourceLang the source language code
     */
    public void setSourceLang(String sourceLang) {
        this.sourceLang = sourceLang;
    }

    /**
     * Gets the target language code.
     *
     * @return the target language code
     */
    public String getTargetLang() {
        return targetLang;
    }

    /**
     * Sets the target language code.
     *
     * @param targetLang the target language code
     */
    public void setTargetLang(String targetLang) {
        this.targetLang = targetLang;
    }
}
