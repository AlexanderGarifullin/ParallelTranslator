package org.tba.paralleltranslator.translationclient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.tba.paralleltranslator.utils.ErrorMessages;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MyMemoryTranslationClientTest {
    @InjectMocks
    private MyMemoryTranslationClient myMemoryTranslationClient;

    @Mock
    private RestTemplate restTemplate;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void translate_successfulTranslation_returnsTranslatedText() {
        String text = "Hello";
        String sourceLang = "en";
        String targetLang = "es";
        String translatedText = "Hola";

        MyMemoryTranslationClient.TranslationResponse response = new MyMemoryTranslationClient.TranslationResponse();
        MyMemoryTranslationClient.ResponseData responseData = new MyMemoryTranslationClient.ResponseData();
        responseData.setTranslatedText(translatedText);
        response.setResponseData(responseData);


        when(restTemplate.getForEntity(anyString(), eq(MyMemoryTranslationClient.TranslationResponse.class),
                anyString(), anyString(), anyString()))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        String result = myMemoryTranslationClient.translate(text, sourceLang, targetLang);
        assertEquals("Hola", result);
    }

    @Test
    public void translate_restTemplateThrowsException_throwsRuntimeException_GENERAL_API_ERROR() {
        String text = "Hello";
        String sourceLang = "en";
        String targetLang = "es";

        when(restTemplate.getForEntity(anyString(), eq(MyMemoryTranslationClient.TranslationResponse.class),
                anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("RestTemplate exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                myMemoryTranslationClient.translate(text, sourceLang, targetLang));

        assertEquals(ErrorMessages.GENERAL_API_ERROR, exception.getMessage());
    }

    @Test
    public void translate_restTemplateThrowsException_throwsRuntimeException_EMPTY_RESPONSE_BODY() {
        String text = "Hello";
        String sourceLang = "en";
        String targetLang = "es";

        ResponseEntity<MyMemoryTranslationClient.TranslationResponse> responseEntityMock = mock(ResponseEntity.class);

        when(restTemplate.getForEntity(anyString(), eq(MyMemoryTranslationClient.TranslationResponse.class),
                anyString(), anyString(), anyString()))
                .thenReturn(responseEntityMock);

        when(responseEntityMock.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntityMock.getBody()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                myMemoryTranslationClient.translate(text, sourceLang, targetLang)
        );

        assertEquals(ErrorMessages.EMPTY_RESPONSE_BODY, exception.getMessage());
    }

    @Test
    public void translate_restTemplateThrowsException_throwsRuntimeException_API_CALL_FAILURE() {
        String text = "Hello";
        String sourceLang = "en";
        String targetLang = "es";

        ResponseEntity<MyMemoryTranslationClient.TranslationResponse> responseEntityMock = mock(ResponseEntity.class);

        when(restTemplate.getForEntity(anyString(), eq(MyMemoryTranslationClient.TranslationResponse.class),
                anyString(), anyString(), anyString()))
                .thenReturn(responseEntityMock);

        when(responseEntityMock.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                myMemoryTranslationClient.translate(text, sourceLang, targetLang)
        );

        assertEquals(ErrorMessages.API_CALL_FAILURE + "400 BAD_REQUEST", exception.getMessage());
    }
}