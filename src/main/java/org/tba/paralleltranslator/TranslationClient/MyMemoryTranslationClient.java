package org.tba.paralleltranslator.TranslationClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.tba.paralleltranslator.interfaces.TranslationClient;

@Component
public class MyMemoryTranslationClient implements TranslationClient {

    private static final String API_URL = "https://api.mymemory.translated.net/get?q={text}&langpair={source}|{target}";

    private final RestTemplate restTemplate;

    @Autowired
    public MyMemoryTranslationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String translate(String text, String sourceLang, String targetLang) {
        return "testTranslate";
    }
}
