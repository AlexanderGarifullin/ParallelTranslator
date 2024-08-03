package org.tba.paralleltranslator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tba.paralleltranslator.interfaces.TranslationClient;
import org.tba.paralleltranslator.requests.TranslationRequest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class TranslationService {

    private final TranslationClient translationClient;
    private final ExecutorService executorService;

    @Autowired
    public TranslationService(TranslationClient translationClient, ExecutorService executorService) {
        this.translationClient = translationClient;
        this.executorService = executorService;
    }


    public String translate(String text, String sourceLang, String targetLang) throws InterruptedException,
            ExecutionException, IllegalArgumentException {
        String[] words = text.split("\\s+");

        List<Callable<String>> tasks = new ArrayList<>();
        for (String word : words) {
            if (word.getBytes(StandardCharsets.UTF_8).length > 500) {
                throw new IllegalArgumentException("Word exceeds maximum length of 500 bytes: " + word);
            }
            tasks.add(() -> translationClient.translate(word, sourceLang, targetLang));
        }

        List<Future<String>> futures = executorService.invokeAll(tasks);

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            translatedText.append(future.get()).append(" ");
        }

        return translatedText.toString().trim();
    }

    public void processTranslationRequest(TranslationRequest request) {
        processText(request);
    }

    public void processText(TranslationRequest request) {
        String[] words = request.getText().split("\\s+");
        StringBuilder processText = new StringBuilder();

        for (String word : words) {
            processText.append(word.trim()).append(" ");
        }

        request.setText(processText.toString());
    }

    @Async
    public void saveRequest(String clientIp, String text, String translatedText) {
        System.out.println("save");
    }
}
