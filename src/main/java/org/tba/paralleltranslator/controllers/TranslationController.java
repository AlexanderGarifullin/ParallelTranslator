package org.tba.paralleltranslator.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tba.paralleltranslator.requests.TranslationRequest;
import org.tba.paralleltranslator.services.TranslationService;
import org.tba.paralleltranslator.utils.ErrorMessages;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class TranslationController {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/translate")
    public ResponseEntity<String> translate(@RequestBody TranslationRequest request,
                                            HttpServletRequest servletRequest) {

        translationService.processTranslationRequest(request);

        try {
            String translatedText = translationService.translate(request.getText(), request.getSourceLang(), request.getTargetLang());
            translationService.saveRequest(servletRequest.getRemoteAddr(), request.getText(), translatedText);
            return new ResponseEntity<>(translatedText, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(ErrorMessages.ERROR_TRANSLATION_INTERRUPTED, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return new ResponseEntity<>(ErrorMessages.ERROR_EXECUTION_EXCEPTION, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
