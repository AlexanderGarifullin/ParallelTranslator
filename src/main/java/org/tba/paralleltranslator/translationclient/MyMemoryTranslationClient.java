package org.tba.paralleltranslator.translationclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.tba.paralleltranslator.interfaces.TranslationClient;
import org.tba.paralleltranslator.utils.ErrorMessages;

import static org.tba.paralleltranslator.utils.StringMethods.getCleanWorld;
import static org.tba.paralleltranslator.utils.StringMethods.replaceWorld;

/**
 * Implementation of the {@link TranslationClient} interface that uses the MyMemory API for translations.
 */
@Component
public class MyMemoryTranslationClient implements TranslationClient {

    private static final String API_URL = "https://api.mymemory.translated.net/get?q={text}&langpair={source}|{target}";

    private final RestTemplate restTemplate;

    /**
     * Constructs a new MyMemoryTranslationClient with the specified RestTemplate.
     *
     * @param restTemplate the RestTemplate to use for making API requests
     */
    @Autowired
    public MyMemoryTranslationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Translates the given text from the source language to the target language using the MyMemory API.
     *
     * @param text the text to translate
     * @param sourceLang the source language code
     * @param targetLang the target language code
     * @return the translated text
     * @throws RuntimeException if there is an error during API call or if the response is invalid
     */
    @Override
    public String translate(String text, String sourceLang, String targetLang) throws RuntimeException {
        String cleanWorld = getCleanWorld(text);
        if (cleanWorld.isEmpty()) return text;
        ResponseEntity<TranslationResponse> response;

        try {
            response = restTemplate.getForEntity(
                    API_URL,
                    TranslationResponse.class,
                    cleanWorld, sourceLang, targetLang
            );
        } catch (Exception e) {
            throw new RuntimeException(ErrorMessages.GENERAL_API_ERROR, e);
        }

        if (response.getStatusCode() == HttpStatus.OK) {
            TranslationResponse translationResponse = response.getBody();
            if (translationResponse != null) {
                return replaceWorld(text, translationResponse.getResponseData().getTranslatedText());
            } else {
                throw new RuntimeException(ErrorMessages.EMPTY_RESPONSE_BODY);
            }
        } else {
            throw new RuntimeException(ErrorMessages.API_CALL_FAILURE + response.getStatusCode());
        }
    }

    /**
     * Inner class representing the response from the MyMemory API.
     */
    static class TranslationResponse {
        private ResponseData responseData;

        public ResponseData getResponseData() {
            return responseData;
        }

        public void setResponseData(ResponseData responseData) {
            this.responseData = responseData;
        }
    }

    /**
     * Inner class representing the data part of the translation response.
     */
    static class ResponseData {
        private String translatedText;

        public String getTranslatedText() {
            return translatedText;
        }

        public void setTranslatedText(String translatedText) {
            this.translatedText = translatedText;
        }
    }
}
