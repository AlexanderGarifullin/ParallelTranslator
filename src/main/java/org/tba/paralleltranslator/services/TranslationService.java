package org.tba.paralleltranslator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tba.paralleltranslator.interfaces.TranslationClient;

@Service
public class TranslationService {

    private final TranslationClient translationClient;

    @Autowired
    public TranslationService(TranslationClient translationClient) {
        this.translationClient = translationClient;
    }

    public String translate(String text, String sourceLang, String targetLang) {
        return translationClient.translate(text, sourceLang, targetLang);
    }
}
