package org.tba.paralleltranslator.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.tba.paralleltranslator.models.Language;
import org.tba.paralleltranslator.requests.TranslationRequest;
import org.tba.paralleltranslator.services.TranslationService;
import org.tba.paralleltranslator.utils.ErrorMessages;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TranslationControllerTest {

    @InjectMocks
    private TranslationController translationController;

    @Mock
    private TranslationService translationService;

    @Mock
    private HttpServletRequest servletRequest;

    private TranslationRequest request;
    private String translatedText;
    private String clientIp;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        initData();
    }

    @Test
    public void translate_validRequest_returnsTranslatedText() throws Exception {

        when(translationService.translate(request.getText(), request.getSourceLang(), request.getTargetLang()))
                .thenReturn(translatedText);

        when(servletRequest.getRemoteAddr()).thenReturn(clientIp);

        ResponseEntity<String> response = translationController.translate(request, servletRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(translatedText, response.getBody());

        verify(translationService, times(1)).processTranslationRequest(request);
        verify(translationService, times(1)).translate(request.getText(),
                request.getSourceLang(), request.getTargetLang());
        verify(translationService, times(1)).saveRequest(clientIp,
                request.getText(), translatedText);
    }

    @Test
    public void translate_invalidRequest_throwsIllegalArgumentException() throws Exception {

        when(translationService.translate(request.getText(), request.getSourceLang(), request.getTargetLang()))
                .thenThrow(new IllegalArgumentException(ErrorMessages.ERROR_WORD_TOO_LONG));

        ResponseEntity<String> response = translationController.translate(request, servletRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(ErrorMessages.ERROR_WORD_TOO_LONG, response.getBody());

        verify(translationService, times(1)).processTranslationRequest(request);
        verify(translationService, times(1)).translate(request.getText(),
                request.getSourceLang(), request.getTargetLang());
        verify(translationService, times(0)).saveRequest(anyString(), anyString(), anyString());
    }

    @Test
    public void translate_translationInterrupted_throwsInternalServerError() throws Exception {

        when(translationService.translate(request.getText(), request.getSourceLang(), request.getTargetLang()))
                .thenThrow(new InterruptedException());

        ResponseEntity<String> response = translationController.translate(request, servletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorMessages.ERROR_TRANSLATION_INTERRUPTED, response.getBody());

        verify(translationService, times(1)).processTranslationRequest(request);
        verify(translationService, times(1)).translate(request.getText(),
                request.getSourceLang(), request.getTargetLang());
        verify(translationService, times(0)).saveRequest(anyString(), anyString(), anyString());
    }

    @Test
    public void translate_translationExecutionException_throwsInternalServerError() throws Exception {

        when(translationService.translate(request.getText(), request.getSourceLang(), request.getTargetLang()))
                .thenThrow(new ExecutionException(new Throwable()));

        ResponseEntity<String> response = translationController.translate(request, servletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(ErrorMessages.ERROR_EXECUTION_EXCEPTION, response.getBody());

        verify(translationService, times(1)).processTranslationRequest(request);
        verify(translationService, times(1)).translate(request.getText(),
                request.getSourceLang(), request.getTargetLang());
        verify(translationService, times(0)).saveRequest(anyString(), anyString(), anyString());
    }

    @Test
    public void translate_runtimeException_throwsInternalServerError() throws Exception {
        when(translationService.translate(request.getText(), request.getSourceLang(), request.getTargetLang()))
                .thenThrow(new RuntimeException("Some runtime exception"));

        ResponseEntity<String> response = translationController.translate(request, servletRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Some runtime exception", response.getBody());

        verify(translationService, times(1)).processTranslationRequest(request);
        verify(translationService, times(1)).translate(request.getText(),
                request.getSourceLang(), request.getTargetLang());
        verify(translationService, times(0)).saveRequest(anyString(), anyString(), anyString());
    }

    @Test
    public void getLanguages_makeRequest_returnsLanguages() {
        Set<Language> languages = translationController.getLanguages();
        verify(translationService, times(1)).getSupportedLanguages();
    }

    private void initData() {
        request = new TranslationRequest();
        request.setText("Hello World");
        request.setSourceLang("en");
        request.setTargetLang("es");
        translatedText = "Hola Mundo";
        clientIp = "192.168.0.1";
    }
}