package org.tba.paralleltranslator.translationclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.tba.paralleltranslator.interfaces.TranslationClient;
import org.tba.paralleltranslator.utils.ErrorMessages;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                    return getFullWorld(text, translationResponse.getResponseData().getTranslatedText());
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

    private static class TranslationResponse {
        private ResponseData responseData;

        public ResponseData getResponseData() {
            return responseData;
        }

        public void setResponseData(ResponseData responseData) {
            this.responseData = responseData;
        }
    }

    private static class ResponseData {
        private String translatedText;

        public String getTranslatedText() {
            return translatedText;
        }

        public void setTranslatedText(String translatedText) {
            this.translatedText = translatedText;
        }
    }

    static String getCleanWorld(String text) {
        int firstIndex = findFirstLetterIndex(text);
        int lastIndex = findLastLetterIndex(text);

        if (firstIndex == -1 || lastIndex == -1) {
            return "";
        }

        return text.substring(firstIndex, lastIndex + 1);
    }

    static String getFullWorld(String text, String translatedText) {
        int firstIndex = findFirstLetterIndex(text);
        int lastIndex = findLastLetterIndex(text);

        if (firstIndex == -1 || lastIndex == -1) {
            return text;
        }

        String before = text.substring(0, firstIndex);
        String after = text.substring(lastIndex + 1);
        return before + translatedText + after;
    }

    private static int findFirstLetterIndex(String text) {
        String regex = "\\p{L}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        return matcher.find() ? matcher.start() : -1;
    }

    private static int findLastLetterIndex(String text) {
        String regex = "\\p{L}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        int lastIndex = -1;
        while (matcher.find()) {
            lastIndex = matcher.start();
        }
        return lastIndex;
    }
}
