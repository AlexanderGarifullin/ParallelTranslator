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

@Component
public class MyMemoryTranslationClient implements TranslationClient {

    private static final String API_URL = "https://api.mymemory.translated.net/get?q={text}&langpair={source}|{target}";

    private final RestTemplate restTemplate;

    @Autowired
    public MyMemoryTranslationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String translate(String text, String sourceLang, String targetLang) throws RuntimeException {
        try {
            String cleanWorld = getCleanWorld(text);
            if (cleanWorld.isEmpty()) return text;
            ResponseEntity<TranslationResponse> response = restTemplate.getForEntity(
                    API_URL,
                    TranslationResponse.class,
                    cleanWorld, sourceLang, targetLang
            );

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
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(ErrorMessages.GENERAL_API_ERROR, e);
        }
    }

    static class TranslationResponse {
        private ResponseData responseData;

        public ResponseData getResponseData() {
            return responseData;
        }

        public void setResponseData(ResponseData responseData) {
            this.responseData = responseData;
        }
    }

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
