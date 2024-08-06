package org.tba.paralleltranslator.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tba.paralleltranslator.models.Language;
import org.tba.paralleltranslator.requests.TranslationRequest;
import org.tba.paralleltranslator.services.TranslationService;
import org.tba.paralleltranslator.utils.ErrorMessages;

import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Controller for handling translation-related requests.
 */
@Controller
@RequestMapping("/api")
public class TranslationController {

    private final TranslationService translationService;

    /**
     * Constructs a new TranslationController with the specified TranslationService.
     *
     * @param translationService the TranslationService to use for translations
     */
    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    /**
     * Returns the translate page.
     *
     * @return the name of the translate view
     */
    @GetMapping("/translate")
    public String showTranslatePage() {
        return "translate";
    }

    /**
     * Returns the set of supported languages.
     *
     * @return a set of Language objects
     */
    @GetMapping("/languages")
    @ResponseBody
    public Set<Language> getLanguages() {
        return translationService.getSupportedLanguages();
    }

    /**
     * Handles the translation request.
     *
     * @param request the translation request containing the text and languages
     * @param servletRequest the HTTP servlet request to get client IP
     * @return a ResponseEntity containing the translated text or an error message
     */
    @PostMapping("/translate")
    @ResponseBody
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
