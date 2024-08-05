package org.tba.paralleltranslator.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tba.paralleltranslator.interfaces.ApiTranslationRequestsLogsDAO;
import org.tba.paralleltranslator.interfaces.TranslationClient;
import org.tba.paralleltranslator.models.Language;
import org.tba.paralleltranslator.requests.TranslationRequest;
import org.tba.paralleltranslator.utils.ErrorMessages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

class TranslationServiceTest {

    @InjectMocks
    private TranslationService translationService;

    @Mock
    private TranslationClient translationClient;

    @Mock
    private ExecutorService executorService;

    @Mock
    private ApiTranslationRequestsLogsDAO apiTranslationRequestsLogsDAO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        translationService = spy(new TranslationService(translationClient, executorService, apiTranslationRequestsLogsDAO));
    }

    @Test
    public void translate_validText_successfullyTranslatesText() throws Exception {

        String text = "Hello World";
        String sourceLang = "en";
        String targetLang = "es";
        String translatedText = "Hola Mundo";

        when(translationClient.translate("Hello", sourceLang, targetLang)).thenReturn("Hola");
        when(translationClient.translate("World", sourceLang, targetLang)).thenReturn("Mundo");

        Future<String> future1 = mock(Future.class);
        Future<String> future2 = mock(Future.class);
        when(future1.get()).thenReturn("Hola");
        when(future2.get()).thenReturn("Mundo");


        List<Future<String>> futures = new ArrayList<>();
        futures.add(future1);
        futures.add(future2);

        List<Callable<String>> taskList = mock(List.class);
        when(translationService.getTasks(text, sourceLang, targetLang)).thenReturn(taskList);

        when(executorService.invokeAll(taskList)).thenReturn(futures);

        String result = translationService.translate(text, sourceLang, targetLang);

        assertEquals(translatedText, result);
    }

    @Test
    public void translate_wordTooLong_throwsIllegalArgumentException() {

        String longWord = "a".repeat(501);
        String text = longWord;
        String sourceLang = "en";
        String targetLang = "es";


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                translationService.translate(text, sourceLang, targetLang)
        );
        assertEquals(ErrorMessages.ERROR_WORD_TOO_LONG, exception.getMessage());
    }

    @Test
    public void translate_executionException_throwsExecutionException() throws Exception {

        String text = "Hello World";
        String sourceLang = "en";
        String targetLang = "es";

        Future<String> future1 = mock(Future.class);
        Future<String> future2 = mock(Future.class);
        when(future1.get()).thenThrow(new ExecutionException(new Throwable()));
        when(future2.get()).thenThrow(new ExecutionException(new Throwable()));

        List<Future<String>> futures = new ArrayList<>();
        futures.add(future1);
        futures.add(future2);

        List<Callable<String>> taskList = mock(List.class);
        when(translationService.getTasks(text, sourceLang, targetLang)).thenReturn(taskList);
        when(executorService.invokeAll(taskList)).thenReturn(futures);


        ExecutionException exception = assertThrows(ExecutionException.class, () ->
                translationService.translate(text, sourceLang, targetLang)
        );
        assertEquals("java.util.concurrent.ExecutionException", exception.getClass().getName());
    }

    @Test
    public void translate_interruptedException_throwsInterruptedException() throws Exception {

        String text = "Hello World";
        String sourceLang = "en";
        String targetLang = "es";

        List<Callable<String>> taskList = mock(List.class);
        when(translationService.getTasks(text, sourceLang, targetLang)).thenReturn(taskList);
        when(executorService.invokeAll(taskList)).thenThrow(new InterruptedException());

        InterruptedException exception = assertThrows(InterruptedException.class, () ->
                translationService.translate(text, sourceLang, targetLang)
        );
        assertEquals("java.lang.InterruptedException", exception.getClass().getName())
        ;
    }

    @Test
    public void processText_validText_processesTextCorrectly() {

        TranslationRequest request = new TranslationRequest();
        request.setText("  Hello   World!  ");

        translationService.processText(request);

        assertEquals("Hello World!", request.getText());

        verify(translationService, times(1)).processText(request);
    }

    @Test
    public void saveRequest_methodCalled_correctArguments() {

        String clientIp = "192.168.0.1";
        String text = "Hello World";
        String translatedText = "Hola Mundo";

        translationService.saveRequest(clientIp, text, translatedText);

        verify(translationService, times(1)).saveRequest(clientIp, text, translatedText);
    }

    @Test
    void testGetSupportedLanguages() {
        Set<Language> languages = translationService.getSupportedLanguages();

        assertNotNull(languages, "Languages set should not be null");
        assertFalse(languages.isEmpty(), "Languages set should not be empty");

        String previousName = "";
        for (Language language : languages) {
            assertTrue(language.getName().compareTo(previousName) >= 0,
                    "Languages should be sorted by name");
            previousName = language.getName();
        }

        assertTrue(languages.stream().anyMatch(l -> l.getCode().equals("en") && l.getName().equals("English")),
                "Languages set should contain English");
        assertTrue(languages.stream().anyMatch(l -> l.getCode().equals("fr") && l.getName().equals("French")),
                "Languages set should contain French");

        Set<String> languageCodes = new HashSet<>();
        for (Language language : languages) {
            assertTrue(languageCodes.add(language.getCode()), "Language codes should be unique");
        }
    }
}