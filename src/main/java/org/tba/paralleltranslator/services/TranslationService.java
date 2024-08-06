package org.tba.paralleltranslator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.tba.paralleltranslator.interfaces.ApiTranslationRequestsLogsDAO;
import org.tba.paralleltranslator.interfaces.TranslationClient;
import org.tba.paralleltranslator.models.Language;
import org.tba.paralleltranslator.requests.TranslationRequest;
import org.tba.paralleltranslator.utils.ErrorMessages;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Service for handling translation requests and interacting with the translation client and database.
 */
@Service
public class TranslationService {

    private final TranslationClient translationClient;
    private final ExecutorService executorService;
    private final ApiTranslationRequestsLogsDAO apiTranslationRequestsLogsDAO;

    /**
     * Constructs a new TranslationService with the specified dependencies.
     *
     * @param translationClient the client to use for translations
     * @param executorService the executor service to use for managing translation tasks
     * @param apiTranslationRequestsLogsDAO the DAO to use for logging translation requests
     */
    @Autowired
    public TranslationService(TranslationClient translationClient, ExecutorService executorService, ApiTranslationRequestsLogsDAO apiTranslationRequestsLogsDAO) {
        this.translationClient = translationClient;
        this.executorService = executorService;
        this.apiTranslationRequestsLogsDAO = apiTranslationRequestsLogsDAO;
    }

    /**
     * Translates the given text from source language to target language.
     *
     * @param text the text to translate
     * @param sourceLang the source language
     * @param targetLang the target language
     * @return the translated text
     * @throws InterruptedException if the translation process is interrupted
     * @throws ExecutionException if the translation process fails
     * @throws IllegalArgumentException if the input text contains words that are too long
     * @throws RuntimeException if a runtime exception occurs
     */
    public String translate(String text, String sourceLang, String targetLang) throws InterruptedException,
            ExecutionException, IllegalArgumentException, RuntimeException {

        List<Callable<String>> tasks = getTasks(text, sourceLang, targetLang);

        List<Future<String>> futures = executorService.invokeAll(tasks);

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            translatedText.append(future.get()).append(" ");
        }

        return translatedText.toString().trim();
    }

    /**
     * Creates a list of translation tasks for each word in the text.
     *
     * @param text the text to translate
     * @param sourceLang the source language
     * @param targetLang the target language
     * @return a list of Callable tasks
     * @throws IllegalArgumentException if any word in the text is too long
     */
    List<Callable<String>> getTasks(String text, String sourceLang, String targetLang) throws IllegalArgumentException{

        String[] words = text.split("\\s+");

        List<Callable<String>> tasks = new ArrayList<>();
        for (String word : words) {
            if (word.getBytes(StandardCharsets.UTF_8).length > 500) {
                throw new IllegalArgumentException(ErrorMessages.ERROR_WORD_TOO_LONG);
            }
            tasks.add(() -> translationClient.translate(word, sourceLang, targetLang));
        }
        return tasks;
    }

    /**
     * Processes the translation request by normalizing the text.
     *
     * @param request the translation request to process
     */
    public void processTranslationRequest(TranslationRequest request) {
        processText(request);
    }

    /**
     * Normalizes the text in the translation request by trimming each word.
     *
     * @param request the translation request containing the text to process
     */
    void processText(TranslationRequest request) {
        String[] words = request.getText().split("\\s+");
        StringBuilder processText = new StringBuilder();

        for (String word : words) {
            processText.append(word.trim()).append(" ");
        }

        request.setText(processText.toString().trim());
    }

    /**
     * Asynchronously saves the translation request to the database.
     *
     * @param clientIp the IP address of the client making the request
     * @param text the original text
     * @param translatedText the translated text
     */
    @Async
    public void saveRequest(String clientIp, String text, String translatedText) {
        apiTranslationRequestsLogsDAO.save(clientIp, text, translatedText);
    }

    /**
     * Returns a set of supported languages.
     *
     * @return a sorted set of Language objects
     */
    public Set<Language> getSupportedLanguages(){
        Comparator<Language> languageComparator = Comparator.comparing(Language::getName);
        Set<Language> languages = new TreeSet<>(languageComparator);

        for (Locale locale : Locale.getAvailableLocales()) {
            String languageCode = locale.getLanguage();
            String languageName = locale.getDisplayLanguage(Locale.ENGLISH);
            if (!languageCode.isEmpty() && !languageName.isEmpty()) {
                languages.add(new Language(languageCode, languageName));
            }
        }
        return languages;
    }
}
