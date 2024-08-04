package org.tba.paralleltranslator.interfaces;

public interface TranslationClient {
    String translate(String text, String sourceLang, String targetLang);
}
