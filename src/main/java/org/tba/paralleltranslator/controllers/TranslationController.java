package org.tba.paralleltranslator.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tba.paralleltranslator.services.TranslationService;

@RestController
@RequestMapping("/api")
public class TranslationController {

    private final TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/translate")
    public String translate(@RequestParam String text,
                            @RequestParam String sourceLang,
                            @RequestParam String targetLang,
                            HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        System.out.println(clientIp);
        return translationService.translate(text, sourceLang, targetLang);
    }
}
