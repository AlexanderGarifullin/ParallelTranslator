package org.tba.paralleltranslator.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling errors in the application.
 * Implements Spring's {@link ErrorController} to provide custom error handling.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Handles errors by returning the name of the error view.
     *
     * @return the name of the error view template
     */
    @RequestMapping("/error")
    public String handleError() {
        return "error";
    }
}
